package com.example.healthyeatsuserservice.controllers;

import com.example.healthyeatsuserservice.controllers.responses.APIResponse;
import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.service.UserService;
import com.example.healthyeatsuserservice.controllers.requests.RegistrationRequest;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userService/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        UserDTO userDTO = userService.registerIndividual(registrationRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/plan/{planId}")
    public ResponseEntity<?> addUserToPlan(@PathVariable String planId, @PathVariable String userId) {
        try {
            userService.subscribeUserToMealPlan(userId, planId);
            return new ResponseEntity<>(new APIResponse(true, "done"), HttpStatus.OK);
        } catch (UserException | MealPlanException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
}
