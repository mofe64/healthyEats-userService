package com.example.healthyeatsuserservice.service;

import com.example.healthyeatsuserservice.controllers.requests.*;
import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.TokenException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.models.Preference;
import com.example.healthyeatsuserservice.models.Token;
import com.example.healthyeatsuserservice.models.User;
import com.example.healthyeatsuserservice.service.dtos.MealPlanDTO;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    UserDTO registerIndividual(IndividualRegistrationRequest details) throws UserException;

    UserDTO registerOrganization(OrganizationRegistrationRequest details) throws UserException;

    UserDTO registerAdmin(IndividualRegistrationRequest details) throws UserException;

    UserDTO findUserByUserName(String username);

    UserDTO updateUserDetails(String userId, UserDTO userDTO) throws UserException;

    void addPreference(String userId, PreferenceRequest preferenceRequest) throws UserException;

    void removePreference(String userId, PreferenceRequest preferenceRequest) throws UserException;

    MealPlan createCustomMealPlan(String userId,MealPlanDTO mealPlanDTO) throws UserException, MealPlanException;

    List<MealPlan> getUserMealPlans(String userId) throws UserException;

    void subscribeUserToMealPlan(String userId, String planId) throws UserException, MealPlanException;

    void unsubscribeUserFromMealPlan(String userId, String plantId) throws UserException, MealPlanException;

    Token generatePasswordResetToken(String username) throws UserException;

    void resetUserPassword(PasswordResetRequest request, String passwordResetToken) throws UserException, TokenException;

    void updateUserPassword(PasswordChangeRequest request) throws UserException;

    User internalFindUserById(String id);
    User internalFindUserByEmail(String email);
    User internalFindUserByUsername(String username);

}
