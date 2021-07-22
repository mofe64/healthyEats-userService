package com.example.healthyeatsuserservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String companyName;
    private Set<Role> roles = new HashSet<>();
    private String email;
    private String password;
    private List<Preference> preferences;
    @DBRef(db = "healthy-Eats-meals-db")
    private List<MealPlan> plans = new ArrayList<>();



}
