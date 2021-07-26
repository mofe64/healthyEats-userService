package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
