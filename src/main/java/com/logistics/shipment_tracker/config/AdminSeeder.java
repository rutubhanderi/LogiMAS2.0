package com.logistics.shipment_tracker.config;

import com.logistics.shipment_tracker.model.Role;
import com.logistics.shipment_tracker.model.User;
import com.logistics.shipment_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@logistics.com";

        // Check if admin already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create new admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin1234")); // strong password
            admin.setRole(Role.ADMIN);

            // Save admin to database
            userRepository.save(admin);
            System.out.println("Admin user created: " + adminEmail);
        }
    }
}
