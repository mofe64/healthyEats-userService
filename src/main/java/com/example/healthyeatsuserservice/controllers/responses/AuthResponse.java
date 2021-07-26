package com.example.healthyeatsuserservice.controllers.responses;

import com.example.healthyeatsuserservice.models.Token;
import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private Token refreshToken;
}
