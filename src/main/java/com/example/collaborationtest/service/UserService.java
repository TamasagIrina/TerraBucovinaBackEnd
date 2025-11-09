package com.example.collaborationtest.service;

import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public User getUser(String email) {
        return userRepo.findByEmail(email);
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    public int getIdByEmail(String email) {
        User user = this.getUser(email);
        return user.getId();
    }

    public int getIdByUserName(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
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

        if(authentication.isAuthenticated()) {
            System.out.println("MERGE");

            return jwtService.generateToken(userDetails);
        }

        return "Verification failed";
    }

    public Set<Role> getRole(String username) {
        User user = this.getUser(username);
        return user.getRoles();

    }

    public User deleteUser(String email) {
        User user = this.getUser(email);
        System.out.println("Trying to delete user with email: " + email);
        userRepo.delete(user);
        return user;
    }
}
