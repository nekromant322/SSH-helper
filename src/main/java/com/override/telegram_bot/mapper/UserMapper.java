package com.override.telegram_bot.mapper;


import com.override.telegram_bot.dto.UserDTO;
import com.override.telegram_bot.model.Role;
import com.override.telegram_bot.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO userToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getRoles(),
                user.getServers()
        );
    }

    public static User userDTOToUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getPassword(),
                rolesMapper(userDTO.getRoles()),
                userDTO.getServers()
        );
    }

    private static Set<Role> rolesMapper(Set<Role> roleSet) {
        return roleSet.stream().peek(role -> {
            switch (role.getRole()) {
                case "ROLE_USER":
                    role.setId(1L);
                    break;
                case "ROLE_ADMIN":
                    role.setId(2L);
                    break;
            }
        }).collect(Collectors.toSet());
    }
}
