package com.example.collaborationtest.service;

import com.example.collaborationtest.model.User;
import com.example.collaborationtest.model.UserPrincipal;
import com.example.collaborationtest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {


    private UserRepo userRepo;

    @Autowired
    public MyUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    //CREATED A SERVICE THAT IMPLEMENTS USERDETAILSERVICE TO RETURN AN OBJECT WITH ITS IMPLEMENTATION
    //CHECKING IF THE USERNAME OF THE SEND USER FROM LOGIN FROM IS FOUND IN THE DATABASE
    //IF SO WE RETURN AN OBJECT (USERPRINCIPAL) WITH ALL (PASS, USERNAME, ROLES ETC)

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if(user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException(email);
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();



        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }
}
