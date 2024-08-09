package com.override.telegram_bot.mapper;


import com.override.telegram_bot.dto.UserDTO;
import com.override.telegram_bot.model.User;

public class UserMapper {

    public static UserDTO userToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getServers()
        );
    }

    public static User userDTOToUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getServers()
        );
    }
}
