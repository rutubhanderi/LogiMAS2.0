package com.logistics.shipment_tracker.service;

import com.logistics.shipment_tracker.config.security.CustomUserDetails;
import com.logistics.shipment_tracker.dto.UserDto;
import com.logistics.shipment_tracker.exception.ResourceNotFoundException;
import com.logistics.shipment_tracker.model.User;
import com.logistics.shipment_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public void deleteUser(Long userId, CustomUserDetails userDetails) throws AccessDeniedException {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Prevent deleting other admins
        if (targetUser.getRole().name().equalsIgnoreCase("ADMIN")) {
            throw new AccessDeniedException("Admins cannot be deleted");
        }

        userRepository.deleteById(userId);
    }


    public User updateUser(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Update only non-null fields
        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            existingUser.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
        	existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (userDto.getRole() != null && (userDto.getRole() != null)) {
            existingUser.setRole(userDto.getRole());
        }

        return userRepository.save(existingUser);
    }

}
