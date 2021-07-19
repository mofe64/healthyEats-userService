package com.example.healthyeatsuserservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String companyName;
    private Role role;
    private String email;
    private String password;
    private List<Preference> preferences;
    @DBRef(db = "healthy-Eats-meals-db")
    private List<MealPlan> plans;



}
