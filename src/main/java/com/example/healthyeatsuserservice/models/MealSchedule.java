package com.example.healthyeatsuserservice.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;


@Data
public class MealSchedule {
    @DBRef(db = "healthy-Eats-meals-db")
    private Meal breakfast;
    @DBRef(db = "healthy-Eats-meals-db")
    private Meal lunch;
    @DBRef(db = "healthy-Eats-meals-db")
    private Meal dinner;
}
