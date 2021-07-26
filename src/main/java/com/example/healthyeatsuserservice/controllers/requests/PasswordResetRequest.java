package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String username;
    private String newPassword;
}
