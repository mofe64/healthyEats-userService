package com.example.healthyeatsuserservice.controllers.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIResponse {
    private boolean isSuccessful;
    private String message;
    private LocalDateTime timeStamp;
    private String extraInfo;

    public APIResponse(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        timeStamp = LocalDateTime.now();
    }

    public APIResponse(boolean isSuccessful, String message, String extraInfo) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        timeStamp = LocalDateTime.now();
        this.extraInfo = extraInfo;
    }
}
