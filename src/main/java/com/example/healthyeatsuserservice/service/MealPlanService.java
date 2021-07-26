package com.example.healthyeatsuserservice.service;

import com.example.healthyeatsuserservice.models.User;
import com.example.healthyeatsuserservice.service.dtos.MealPlanDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("healthyeats-mealservice")
public interface MealPlanService {
    @PostMapping(value = "/mealService/api/v1/mealPlans/{planId}/add", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> addUserToMealPlan(@PathVariable String planId, @RequestBody User user);

    @PostMapping(value = "/mealService/api/v1/mealPlans/{planId}/remove", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> removeUserFromMealPlan(@PathVariable String planId, @RequestBody User user);

    @PostMapping(value = "/mealService/api/v1/mealPlans/custom", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> createCustomMealPlan(@RequestBody MealPlanDTO planDTO);

    @DeleteMapping(value = "/mealService/api/v1/mealPlans/{planId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> deleteMealPlan(@PathVariable String planId);

}
