package com.example.spebackend.service;

import com.example.spebackend.model.Customer;
import com.example.spebackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    public final CustomerRepository customerRepository;


    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public String saveNewCustomer(Customer customer){
        Customer customer1 = customerRepository.findByEmail(customer.getEmail()).orElse(null);
        if(customer1 == null) {
            customer1 = new Customer();
            customer1.setPassword(customer.getPassword());
            customer1.setEmail(customer.getEmail());
            customer1.setName(customer.getName());
            customerRepository.save(customer1);
            return ("User Registration was Successful!!");
        }
        else
            return ("User Email ID already exist");
    }

    @Override
    public String verifyLogin(Customer customer) {
        Customer customer1 = customerRepository.findByEmail(customer.getEmail()).orElse(null);
        if(customer1 != null)
        {
            if(customer1.getPassword().equals(customer.getPassword()))
            return ("Login Successfull!");
            else return ("Incorrect Password!");
        }
        else return ("Email Not Registered!");
    }

    @Override
    public Optional<Customer> getCustomerFromDb(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer findByEmail(String email) {
        Customer patient = customerRepository.findByEmail(email).orElse(null);
        return patient;
    }
}
