package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String firstname;
    private String lastname;
    private String password;

}
