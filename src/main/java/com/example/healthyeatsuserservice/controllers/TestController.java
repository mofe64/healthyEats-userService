package com.example.healthyeatsuserservice.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", maxAge = 3600)

public class TestController {
    @GetMapping("")
    public String userServiceStartUP() {
        return "User-Service Up and Running ......";
    }
}
