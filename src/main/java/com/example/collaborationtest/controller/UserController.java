package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.UserRepo;
import com.example.collaborationtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


//    @PostMapping("/login")
//    public String login(@RequestBody User user) {
//        System.out.println("login");
//        return userService.verify(user);
//    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
