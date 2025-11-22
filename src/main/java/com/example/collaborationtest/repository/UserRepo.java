package com.example.collaborationtest.repository;

import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);

    List<User> findAllByRoles(Role role);

}
