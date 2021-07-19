package com.example.healthyeatsuserservice.service;

import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.models.Preference;
import com.example.healthyeatsuserservice.controllers.requests.RegistrationRequest;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO registerIndividual(RegistrationRequest details);
    UserDTO updateUserDetails(String userId, UserDTO userDTO);
    void addPreference(String userId, Preference newPreference);
    void removePreference(String userId, Preference preference);
    MealPlan createCustomMealPlan();
    List<MealPlan> getUserMealPlans(String userId);
    void subscribeUserToMealPlan(String userId, String planId) throws UserException, MealPlanException;
    void unsubscribeUserFromMealPlan(String userId, String plantId);
}
