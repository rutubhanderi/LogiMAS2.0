package com.logistics.shipment_tracker.service;

import com.logistics.shipment_tracker.dto.UserDto;
import com.logistics.shipment_tracker.exception.ResourceNotFoundException;
import com.logistics.shipment_tracker.model.User;
import com.logistics.shipment_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    public User updateUser(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());

        return userRepository.save(existingUser);
    }
}
