package com.example.collaborationtest.model;

import com.example.collaborationtest.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.time.LocalDateTime;
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

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "address")
    private String address;

    /**
     * Encoded new password awaiting email confirmation. Only copied over to
     * {@code password} once the user clicks the confirmation link.
     */
    @Column(name = "pending_password")
    @JsonIgnore
    private String pendingPassword;

    @Column(name = "password_reset_token")
    @JsonIgnore
    private String passwordResetToken;

    @Column(name = "password_reset_token_expiry")
    @JsonIgnore
    private LocalDateTime passwordResetTokenExpiry;

    @Column(name = "terms_accepted")
    private boolean termsAccepted;
    @Column(name = "terms_accepted_at")
    private LocalDateTime termsAcceptedAt;
    @Column(name = "terms_version")
    private String termsVersion;

    @Column(name = "email_confirmed")
    private boolean emailConfirmed = false;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    private boolean enabled = true;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Review> reviews;


}
