package com.example.spebackend.controller;

import com.example.spebackend.model.Customer;
import com.example.spebackend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private LoginController loginController;
    @Test
    void loginPatient() {
        Customer customer = new Customer();
        // Set customer properties for login

        // Mocking service response
        String expectedResponse = "Login successful";
        when(customerService.verifyLogin(customer)).thenReturn(expectedResponse);

        // Call the method under test
        String actualResponse = loginController.loginPatient(customer);

        // Assertions
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void registerCustomer() {
        Customer customer = new Customer();
        // Set customer properties for registration

        // Mocking service response
        Customer savedCustomer = new Customer();
        // Set saved customer properties
        ResponseEntity<Customer> expectedResponse = ResponseEntity.ok(savedCustomer);
        when(customerService.saveNewCustomer(customer)).thenReturn(String.valueOf(savedCustomer));

        // Call the method under test
        ResponseEntity<?> actualResponse = loginController.registerCustomer(customer);

        // Assertions
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        // Additional assertions based on the response content can be added
    }
}