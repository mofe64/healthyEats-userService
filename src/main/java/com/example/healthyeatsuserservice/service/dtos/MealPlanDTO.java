package com.example.healthyeatsuserservice.service.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class MealPlanDTO {
    private String id;
    private String name;
    private int durationInDays;
    private boolean active;
    private boolean dailyDropOff;
    private boolean weeklyDropOff;
    private String type;
    private Map<Integer, MealScheduleDTO> meals;
    private boolean custom;
}
