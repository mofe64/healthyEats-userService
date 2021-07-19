package com.example.healthyeatsuserservice.service.dtos;

import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.models.Preference;
import com.example.healthyeatsuserservice.models.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String companyName;
    private Role role;
    private String email;
    private String password;
    private List<Preference> preferences;
    private List<MealPlan> plans;
}
