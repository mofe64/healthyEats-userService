package com.example.healthyeatsuserservice.service;

import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.models.Preference;
import com.example.healthyeatsuserservice.models.User;
import com.example.healthyeatsuserservice.repository.UserRepository;
import com.example.healthyeatsuserservice.controllers.requests.RegistrationRequest;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO registerIndividual(RegistrationRequest details) {
        User user = new User();
        user.setFirstname(details.getFirstname());
        user.setLastname(details.getLastname());
        user.setPassword(details.getPassword());
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);

    }

    @Override
    public UserDTO updateUserDetails(String userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public void addPreference(String userId, Preference newPreference) {

    }

    @Override
    public void removePreference(String userId, Preference preference) {

    }

    @Override
    public MealPlan createCustomMealPlan() {
        return null;
    }

    @Override
    public List<MealPlan> getUserMealPlans(String userId) {
        return null;
    }

    @Override
    public void subscribeUserToMealPlan(String userId, String planId) throws UserException, MealPlanException {
        User user = findUserById(userId);
        ResponseEntity<?> response = mealPlanService.addUserToMealPlan(planId, user);
        log.info("body ---> {}", response.getBody());
        if(response.getStatusCode().is4xxClientError()) {
            throw new MealPlanException("No mealPlan found with that Id");
        }
        if(response.getStatusCode().is5xxServerError()){
            throw new MealPlanException("Looks like something went wrong, please try again");
        }
        MealPlan plan = new MealPlan();
        modelMapper.map(response.getBody(), plan);
        user.getPlans().add(plan);
        userRepository.save(user);
    }

    @Override
    public void unsubscribeUserFromMealPlan(String userId, String plantId) {

    }

    private User findUserById(String id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new UserException("No user found with Id " + id)
                );
    }
}
