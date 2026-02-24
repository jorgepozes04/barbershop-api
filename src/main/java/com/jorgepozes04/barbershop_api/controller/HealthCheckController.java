package com.jorgepozes04.barbershop_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public String getHealth() {
        return "OK";
    }
}
