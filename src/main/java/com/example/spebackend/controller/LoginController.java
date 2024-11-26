package com.example.spebackend.controller;

import com.example.spebackend.model.Customer;
import com.example.spebackend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final CustomerService customerService;

    public LoginController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public String loginPatient(@RequestBody Customer customer) {
        String response = customerService.verifyLogin(customer);
        return response;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        System.out.println("\n\nInside Controller\n\n");
        return ResponseEntity.ok(customerService.saveNewCustomer(customer));
    }
}
