package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class OrganizationRegistrationRequest {
    @NotBlank(message = "company name cannot be blank")
    private String companyName;
    @Email
    private String email;
    //TODO add password length validation
    private String password;
}
