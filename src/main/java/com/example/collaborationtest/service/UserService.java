package com.example.collaborationtest.service;

import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepo userRepo;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepo userRepo,
                       AuthenticationManager authenticationManager,
                       JWTService jwtService) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUser(String username) {

        return userRepo.findByUsername(username);
    }

    public int getIdByName(String name) {
        User user = this.getUser(name);
        return user.getId();
    }

    public User createUser(User user) {

        if(getUser(user.getUsername()) != null) {
            System.out.println("User already exists");
            return null;
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(User user) {


        //getting the authentication for the user passed in the login form
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails.getUsername());
        if(authentication.isAuthenticated()) {
            System.out.println("MERGE");

            return jwtService.generateToken(userDetails);
        }

        return "Verification failed";
    }
}
