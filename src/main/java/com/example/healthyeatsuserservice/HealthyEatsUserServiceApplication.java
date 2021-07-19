package com.example.healthyeatsuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HealthyEatsUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthyEatsUserServiceApplication.class, args);
    }

}
