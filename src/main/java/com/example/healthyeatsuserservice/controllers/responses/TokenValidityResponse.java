package com.example.healthyeatsuserservice.controllers.responses;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenValidityResponse implements Serializable {
    private boolean isValid;
    private String userName;
    private String userId;

    public TokenValidityResponse(boolean isValid) {
        this.isValid = isValid;
    }

    public TokenValidityResponse(boolean isValid, String userName, String userId) {
        this.isValid = isValid;
        this.userName = userName;
        this.userId = userId;
    }
}
