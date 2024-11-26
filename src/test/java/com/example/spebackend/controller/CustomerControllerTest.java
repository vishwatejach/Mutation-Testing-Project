package com.example.spebackend.controller;

import com.example.spebackend.model.Booking;
import com.example.spebackend.model.Customer;
import com.example.spebackend.repository.BookingRepository;
import com.example.spebackend.repository.CustomerRepository;
import com.example.spebackend.service.BookingService;
import com.example.spebackend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerController customerController;
    @Test
    void getCustomers() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        when(customerService.getCustomerFromDb(customerId)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerController.getCustomers(customerId);

        assertEquals(customer, result.orElse(null));
        verify(customerService, times(1)).getCustomerFromDb(customerId);
    }


    @Test
    void getcsid() {
        String email = "test@example.com";
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        when(customerService.findByEmail(email)).thenReturn(customer);

        Long result = customerController.getcsid(email);

        assertEquals(customerId, result);
        verify(customerService, times(1)).findByEmail(email);
    }

    @Test
    void getallbookings() {
        Long customerId = 1L;
        Booking booking = new Booking();
        Customer customer1 = new Customer();
        customer1.setId(customerId);
        booking.setCustomer(customer1);
        List<Booking> bookings = Collections.singletonList(booking);
        when(bookingService.getCallHistoryForDoctor(customerId)).thenReturn(bookings);

        List<Booking> result = customerController.getallbookings(customerId);

        assertEquals(bookings, result);
        verify(bookingService, times(1)).getCallHistoryForDoctor(customerId);
    }

    @Test
    void delPatApt() {
        Long aptId = 1L;
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Booking with ID " + aptId + " deleted successfully");
        doNothing().when(bookingRepository).deleteById(aptId);

        String result = customerController.delPatApt(aptId);

        assertEquals(expectedResponse.getBody(), result);
        verify(bookingRepository, times(1)).deleteById(aptId);
    }

}