package com.override.telegram_bot.controller;


import com.override.telegram_bot.dto.UserDTO;
import com.override.telegram_bot.mapper.UserMapper;
import com.override.telegram_bot.model.User;
import com.override.telegram_bot.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userServiceImpl.findAllUsers());
        return "admin-panel";
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userServiceImpl.findUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/allUsers")
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        return userServiceImpl.findAllUsers().stream().map(UserMapper::userToUserDTO).toList();
    }

    //TODO exceptions
    @PatchMapping("/users/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        userServiceImpl.updateUser(UserMapper.userDTOToUser(userDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}