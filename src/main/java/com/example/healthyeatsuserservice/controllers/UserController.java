package com.example.healthyeatsuserservice.controllers;


import com.example.healthyeatsuserservice.controllers.requests.PasswordChangeRequest;
import com.example.healthyeatsuserservice.controllers.requests.PreferenceRequest;
import com.example.healthyeatsuserservice.controllers.responses.APIResponse;
import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.service.UserService;

import com.example.healthyeatsuserservice.service.dtos.MealPlanDTO;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userService/api/v1/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_ADMIN')")
    @PostMapping("/{userId}/plan/add/{planId}")
    public ResponseEntity<?> addUserToPlan(@PathVariable String planId, @PathVariable String userId) {
        try {
            userService.subscribeUserToMealPlan(userId, planId);
            return new ResponseEntity<>(new APIResponse(true, "added user successfully"), HttpStatus.OK);
        } catch (UserException | MealPlanException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_SUPER_ADMIN')")
    @PatchMapping("{userId}")
    public ResponseEntity<?> updateUserDetails(@PathVariable String userId, @RequestBody UserDTO details) {
        try {
            UserDTO updatedUser = userService.updateUserDetails(userId, details);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_ADMIN')")
    @PostMapping("/{userId}/plan/remove/{planId}")
    public ResponseEntity<?> removeUserFromPlan(@PathVariable String planId, @PathVariable String userId) {
        try {
            userService.unsubscribeUserFromMealPlan(userId, planId);
            return new ResponseEntity<>(new APIResponse(true, "removed user successfully"), HttpStatus.OK);
        } catch (UserException | MealPlanException exception) {
            return new ResponseEntity<>(new APIResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_ADMIN')")
    @GetMapping("/{userId}/plans")
    public ResponseEntity<?> getUsersPlans(@PathVariable String userId) {
        try {
            List<MealPlan> userPlans = userService.getUserMealPlans(userId);
            return new ResponseEntity<>(userPlans, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION')")
    @PostMapping("/{userId}/plans")
    public ResponseEntity<?> createCustomMealPlan(@PathVariable String userId, @RequestBody MealPlanDTO planDTO) {
        try {
            MealPlan plan = userService.createCustomMealPlan(userId, planDTO);
            return new ResponseEntity<>(plan, HttpStatus.CREATED);
        } catch (UserException | MealPlanException exception) {
            return new ResponseEntity<>(new APIResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION')")
    @DeleteMapping("/{userId}/plans/{planId}")
    public ResponseEntity<?> cancelCustomMealPlan(@PathVariable String planId, @PathVariable String userId) {
        try {
            userService.cancelCustomMealPlan(userId, planId);
            return new ResponseEntity<>(new APIResponse(true, "Plan cancelled successfully"), HttpStatus.OK);
        } catch (UserException | MealPlanException exception) {
            return new ResponseEntity<>(new APIResponse(false, exception.getMessage()), HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION')")
    @PatchMapping("/{userId}/preferences/add")
    public ResponseEntity<?> addUserPreference(@RequestBody PreferenceRequest request, @PathVariable String userId) {
        try {
            userService.addPreference(userId, request);
            return new ResponseEntity<>(new APIResponse(true, "Preference List added successfully"), HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION')")
    @PatchMapping("/{userId}/preferences/remove")
    public ResponseEntity<?> removeUserPreference(@RequestBody PreferenceRequest request, @PathVariable String userId) {
        try {
            userService.removePreference(userId, request);
            return new ResponseEntity<>(new APIResponse(true, "Preference removed successfully"), HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new APIResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        try {
            userService.updateUserPassword(request);
            return new ResponseEntity<>(new APIResponse(true, "Password changed successfully"), HttpStatus.OK);
        } catch (UserException exception) {
            return new ResponseEntity<>(new APIResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


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
