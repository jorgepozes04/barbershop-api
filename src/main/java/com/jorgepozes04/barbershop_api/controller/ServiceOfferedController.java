package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.service.ServiceOfferedService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
@AllArgsConstructor
public class ServiceOfferedController {
    private final ServiceOfferedService serviceService;
}
