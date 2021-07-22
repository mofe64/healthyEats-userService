package com.example.healthyeatsuserservice.controllers;

import com.example.healthyeatsuserservice.controllers.requests.IndividualRegistrationRequest;
import com.example.healthyeatsuserservice.controllers.requests.OrganizationRegistrationRequest;
import com.example.healthyeatsuserservice.controllers.responses.APIResponse;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.service.UserService;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import com.example.healthyeatsuserservice.service.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userService/api/v1/user/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register/individual")
    public ResponseEntity<?> registerIndividual(@RequestBody @Valid IndividualRegistrationRequest individualRegistrationRequest) {
        try {
            UserDTO userDTO = userService.registerIndividual(individualRegistrationRequest);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/organization")
    public ResponseEntity<?> registerOrganization(@RequestBody @Valid OrganizationRegistrationRequest organizationRegistrationRequest) {
        try {
            UserDTO userDTO = userService.registerOrganization(organizationRegistrationRequest);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    //Todo login

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        return errors;
    }
}
