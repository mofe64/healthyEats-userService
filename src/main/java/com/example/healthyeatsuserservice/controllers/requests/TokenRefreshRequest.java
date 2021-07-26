package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "refreshToken cannot be blank")
    private String refreshToken;
}
