package com.logistics.shipment_tracker.controller;

import com.logistics.shipment_tracker.model.User;
import com.logistics.shipment_tracker.security.CustomUserDetails;
import com.logistics.shipment_tracker.security.CustomUserDetailsService;
import com.logistics.shipment_tracker.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        CustomUserDetails userDetails = (CustomUserDetails)
                userDetailsService.loadUserByUsername(loginRequest.getEmail());

        String jwt = jwtUtil.generateToken(userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority());

        return ResponseEntity.ok(jwt);
    }
}
