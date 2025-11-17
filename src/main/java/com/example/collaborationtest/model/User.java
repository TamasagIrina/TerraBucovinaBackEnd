package com.example.collaborationtest.model;

import com.example.collaborationtest.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    private boolean enabled = true;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Review> reviews;


}
