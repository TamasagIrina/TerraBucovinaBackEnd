package com.example.collaborationtest.controller;

import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.UserRepo;
import com.example.collaborationtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        user.setId(0);
        return userService.createUser(user);
    }

    @GetMapping("/userId/{name}")
    public int getUserByName(@PathVariable String email) {
        return userService.getIdByEmail(email);
    }

    @GetMapping("/getRole/{name}")
    public Set<Role> getRole(@PathVariable String name) {
        return userService.getRole(name);
    }

    @DeleteMapping("/user/delete/{email:.+}")
    public void deleteUser(@PathVariable String email) {
        System.out.println("Trying to delete user with email: " + email);
        userService.deleteUser(email);
    }
}
