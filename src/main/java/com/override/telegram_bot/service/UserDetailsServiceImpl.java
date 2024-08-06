package com.override.telegram_bot.service;


import com.override.telegram_bot.enums.BashCommands;
import com.override.telegram_bot.enums.MessageContants;
import com.override.telegram_bot.model.Role;
import com.override.telegram_bot.model.Server;
import com.override.telegram_bot.model.User;
import com.override.telegram_bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ServerServiceImpl serverService;

    @Autowired
    private SshCommandService sshCommandService;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElseThrow();
    }

    public User findUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByName(username);
        return userOptional.orElseThrow();
    }

    public void saveUser(User user) {
        Optional<User> optionalUser = userRepository.findByName(user.getName());
        if (optionalUser.isPresent()) {
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User updatedUser) {
        User newUser = findUser(updatedUser.getId());
        newUser.setName(updatedUser.getName());
        newUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        newUser.setRoles(updatedUser.getRoles());
        newUser.setServers(updatedUser.getServers());
        userRepository.save(newUser);
    }

    @Transactional
    public void deleteUser(Long id, String serverIp) {
        User user = findUser(id);
        userRepository.deleteById(id);
        String resultCommand = sshCommandService.execCommand(serverIp, String.format(BashCommands.DELUSER, user.getName()));
        System.out.println(resultCommand);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(name);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user.get();
    }

    public String createOrUpdateUserServer(String serverIp, String userName) {
        Server serverFromDB = serverService.findServerByIp(serverIp);
        List<User> users = findAllUsers();
        boolean isNotUserContainsServer = users.stream().anyMatch(user -> !user.getServers().contains(serverFromDB));
        if (isNotUserContainsServer) {
            User existingUser = users.stream().filter(user -> user.getName().equals(userName)).findFirst().get();
            Set<Server> servers = existingUser.getServers();
            servers.add(serverFromDB);
            existingUser.setServers(servers);
            userRepository.save(existingUser);
            return String.format(MessageContants.USER_UPDATE_IN_DB, userName, serverIp);
        }
        User user = new User();
        user.setName(userName);
        user.setPassword(userName + "@" + serverIp);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setServers(Collections.singleton(serverFromDB));
        saveUser(user);
        return String.format(MessageContants.USER_CREATE_IN_DB, userName);
    }
}
