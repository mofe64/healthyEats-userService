package com.example.healthyeatsuserservice.controllers.requests;

import lombok.Data;

import java.util.List;

@Data
public class PreferenceRequest {
    List<String> preferences;
}
