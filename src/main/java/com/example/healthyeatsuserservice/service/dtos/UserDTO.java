package com.example.healthyeatsuserservice.service.dtos;

import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.models.Preference;
import com.example.healthyeatsuserservice.models.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String companyName;
    private Set<Role> roles;
    private String email;
    private String password;
    private List<Preference> preferences;
    private List<MealPlan> plans;
}
