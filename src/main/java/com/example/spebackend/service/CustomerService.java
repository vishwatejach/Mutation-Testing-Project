package com.example.spebackend.service;

import com.example.spebackend.model.Customer;

import java.util.Optional;

public interface CustomerService {
    String saveNewCustomer(Customer customer);

    String verifyLogin(Customer customer);

    Optional<Customer> getCustomerFromDb(Long id);
    Customer findByEmail(String email);
}
