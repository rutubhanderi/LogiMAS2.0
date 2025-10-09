package com.logistics.shipment_tracker.dto;

public class AuthenticationResponse {

    private final String jwt;

    
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    // Getter to retrieve the JWT
    public String getJwt() {
        return jwt;
    }
}
