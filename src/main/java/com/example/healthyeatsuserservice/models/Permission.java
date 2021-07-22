package com.example.healthyeatsuserservice.models;

public enum Permission {
    MEAL_READ("meal:read"),
    MEAL_WRITE("meal:write"),
    MEAL_PLAN_READ("mealPlan:read"),
    MEAL_PLAN_WRITE("mealPlan:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    EVENT_READ("event:read"),
    EVENT_WRITE("event:write"),
    INVENTORY_READ("inventory:read"),
    INVENTORY_WRITE("inventory:write"),
    ALL_ACCESS("all:read:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
