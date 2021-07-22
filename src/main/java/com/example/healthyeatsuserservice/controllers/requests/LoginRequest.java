package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @NotNull
    @NotBlank(message = "please enter your username or email")
    private String username;
    @NotNull
    @NotBlank(message = "please enter your password")
    private String password;
}
