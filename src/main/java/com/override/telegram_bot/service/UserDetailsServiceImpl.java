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

    public void saveUser(User user) {
        Optional<User> optionalUser = userRepository.findByName(user.getName());
        if (optionalUser.isPresent()) {
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(User updatedUser) {
        User newUser = findUser(updatedUser.getId());
        updatedUser.getServers().forEach(s-> System.out.println(s.getIp()));
        newUser.setName(updatedUser.getName());
        newUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        newUser.setRoles(updatedUser.getRoles());
        newUser.setServers(updatedUser.getServers());
        newUser.getServers().stream()
                .filter(s -> updatedUser.getServers().stream().filter(server -> !server.getIp().contains(s.getIp())).isParallel())
                .map(Server::getIp)
                .peek(System.out::println)
                .forEach(ip -> sshCommandService.execCommand(ip, String.format(BashCommands.DELUSER, updatedUser.getName())));
        userRepository.save(newUser);
    }

    public void deleteUser(Long id) {
        User user = findUser(id);
        String username = user.getName();
        user.getServers().stream()
                .map(Server::getIp)
                .forEach(ip -> sshCommandService.execCommand(ip, String.format(BashCommands.DELUSER, username)));
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(name);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user.get();
    }

    //TODO апдейт юзера(добавление ему нового сервера), если он уже есть в базе
    public String createUserServer(String serverIp, String userName) {
        Server serverFromDB = serverService.findServerByIp(serverIp);
        List<User> users = findAllUsers();
        User user = new User();
        user.setName(userName);
        user.setPassword(userName + "@" + serverIp);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setServers(Collections.singleton(serverFromDB));
        saveUser(user);
        return String.format(MessageContants.USER_CREATE_IN_DB, userName);
    }
}
