package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.user.ForgotPasswordRequestDTO;
import com.example.collaborationtest.dto.user.PasswordChangeRequestDTO;
import com.example.collaborationtest.dto.user.UserRequestDTO;
import com.example.collaborationtest.dto.user.UserResponseDTO;
import com.example.collaborationtest.dto.user.UserSelfUpdateDTO;
import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.mapper.UserMapper;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private UserRepo userRepo;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private final UserMapper userMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepo userRepo,
                       AuthenticationManager authenticationManager,
                       JWTService jwtService,
                       UserMapper userMapper) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getUsers() {
        return userMapper.toResponseList(userRepo.findAll());
    }

    /**
     * Entity lookup by email. Kept returning the entity because the security
     * layer (authentication, JWT) needs the full user including credentials.
     */
    public User getUser(String email) {
        return userRepo.findByEmail(email);
    }

    public UserResponseDTO getUserById(Integer id) {
        return userMapper.toResponse(userRepo.findById(id).orElse(null));
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

    /**
     * Controller-facing creation from a validated request payload. Returns the
     * safe read model (never the password), or {@code null} if the user exists.
     */
    public UserResponseDTO createUser(UserRequestDTO request) {
        User saved = createUser(userMapper.toEntity(request));
        return userMapper.toResponse(saved);
    }

    /**
     * Entity-level creation used by the auth flow, which needs to set roles /
     * enabled / terms before persisting. Encodes the password and rejects
     * duplicates (returns {@code null}).
     */
    public User createUser(User user) {
        if (getUser(user.getUsername()) != null) {
            System.out.println("User already exists");
            return null;
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userDetails);
        }

        return "Verification failed";
    }

    /**
     * Confirms a user's email from the token embedded in the confirmation link.
     * Single-use: the token is cleared once consumed. Returns false for an
     * unknown/blank token.
     */
    public boolean confirmEmail(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        User user = userRepo.findByConfirmationToken(token);
        if (user == null) {
            return false;
        }
        user.setEmailConfirmed(true);
        user.setConfirmationToken(null);
        userRepo.save(user);
        return true;
    }

    public Set<Role> getRole(String username) {
        User user = this.getUser(username);
        return user.getRoles();
    }

    public User deleteUser(String email) {
        User user = this.getUser(email);
        userRepo.delete(user);
        return user;
    }

    /**
     * Self-service profile update (username / full name / address) for the
     * currently authenticated user, identified by their email.
     */
    public UserResponseDTO updateSelf(String email, UserSelfUpdateDTO dto) {
        User user = getUser(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (!dto.username().equals(user.getUsername())) {
            User existing = userRepo.findByUsername(dto.username());
            if (existing != null && existing.getId() != user.getId()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
            }
            user.setUsername(dto.username());
        }

        user.setFullName(dto.fullName());
        user.setAddress(dto.address());

        return userMapper.toResponse(userRepo.save(user));
    }

    /**
     * Stages a new password for the given user and returns the freshly
     * generated confirmation token. The password is only applied once
     * {@link #confirmPasswordChange(String)} is called with this token — the
     * caller is responsible for emailing it to the user.
     */
    public String requestPasswordChange(String email, PasswordChangeRequestDTO dto) {
        User user = getUser(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (!encoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parola actuală este incorectă");
        }

        String token = UUID.randomUUID().toString();
        user.setPendingPassword(encoder.encode(dto.newPassword()));
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepo.save(user);

        return token;
    }

    /**
     * Starts a "forgot password" flow: unlike {@link #requestPasswordChange},
     * this does NOT verify the current password (a locked-out user doesn't
     * have it) — knowing the account's email is enough to stage a new one.
     * Returns {@code null} (not an exception) when no account matches the
     * email, so the controller can reply with the same generic message either
     * way and avoid leaking which emails are registered.
     */
    public String requestForgotPassword(ForgotPasswordRequestDTO dto) {
        User user = getUser(dto.email());
        if (user == null) {
            return null;
        }

        String token = UUID.randomUUID().toString();
        user.setPendingPassword(encoder.encode(dto.newPassword()));
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepo.save(user);

        return token;
    }

    /**
     * Confirms a staged password change from the token embedded in the
     * confirmation link. Single-use and time-limited: an unknown, blank, or
     * expired token returns {@code false} without changing anything.
     */
    public boolean confirmPasswordChange(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        User user = userRepo.findByPasswordResetToken(token);
        if (user == null) {
            return false;
        }

        boolean expired = user.getPasswordResetTokenExpiry() == null
                || user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now());

        if (expired) {
            user.setPendingPassword(null);
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiry(null);
            userRepo.save(user);
            return false;
        }

        user.setPassword(user.getPendingPassword());
        user.setPendingPassword(null);
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepo.save(user);
        return true;
    }
}
