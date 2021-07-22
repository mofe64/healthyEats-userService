package com.example.healthyeatsuserservice.service;

import com.example.healthyeatsuserservice.controllers.requests.OrganizationRegistrationRequest;
import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.models.Preference;
import com.example.healthyeatsuserservice.controllers.requests.IndividualRegistrationRequest;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO registerIndividual(IndividualRegistrationRequest details) throws UserException;
    UserDTO registerOrganization(OrganizationRegistrationRequest details) throws UserException;
    UserDTO registerAdmin(IndividualRegistrationRequest details) throws UserException;
    UserDTO updateUserDetails(String userId, UserDTO userDTO);
    void addPreference(String userId, List<String> preference) throws UserException;
    void removePreference(String userId, List<String> preference) throws UserException;
    MealPlan createCustomMealPlan();
    List<MealPlan> getUserMealPlans(String userId) throws UserException;
    void subscribeUserToMealPlan(String userId, String planId) throws UserException, MealPlanException;
    void unsubscribeUserFromMealPlan(String userId, String plantId) throws UserException, MealPlanException;

}
