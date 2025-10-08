package com.logistics.shipment_tracker.controller;

import com.logistics.shipment_tracker.model.User;
import com.logistics.shipment_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Only ADMIN can register new users
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
    
    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found with email: " + email);
        }

        userRepository.delete(user.get());
        return ResponseEntity.ok("User deleted successfully!");
    }

    // Only ADMIN can view all users
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
