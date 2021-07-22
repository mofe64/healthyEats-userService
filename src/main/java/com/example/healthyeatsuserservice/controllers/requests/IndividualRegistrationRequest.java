package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class IndividualRegistrationRequest {
    @NotBlank(message = "first name cannot be blank")
    @Size(min = 2, message = "First name cannot be less than 2 characters")
    private String firstname;
    @NotBlank(message = "last name cannot be blank")
    @Size(min = 2, message = "last name cannot be less than 2 characters")
    private String lastname;
    @NotBlank(message = "username cannot be blank")
    @Size(min = 2, message = "username cannot be less than 2 characters")
    private String username;
    //todo add password size validation
    private String password;
    @NotBlank(message = "email cannot be blank")
    @Email
    private String email;

}
