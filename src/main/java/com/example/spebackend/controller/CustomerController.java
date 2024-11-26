package com.example.spebackend.controller;

import com.example.spebackend.model.Booking;
import com.example.spebackend.model.Customer;
import com.example.spebackend.repository.BookingRepository;
import com.example.spebackend.service.BookingService;
import com.example.spebackend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public CustomerController(CustomerService customerService, BookingService bookingService, BookingRepository bookingRepository) {
        this.customerService = customerService;
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/{id}")
    Optional<Customer> getCustomers(@PathVariable Long id){
        return customerService.getCustomerFromDb(id);
    }

    @GetMapping("/getid")
    public Long getcsid(@RequestParam ("cemail") String cemail){
        Customer customer = customerService.findByEmail(cemail);
        return customer.getId();
    }

    @GetMapping("/getbookings")
    public List<Booking> getallbookings(@RequestParam("cid") Long cid)
    {
        List<Booking> callHistoryList = bookingService.getCallHistoryForDoctor(cid);
        return callHistoryList;
    }
    @PutMapping("/del-apt")
    public String delPatApt(@RequestParam("id") Long aptId) {
        try {
            bookingRepository.deleteById(aptId);
            return "Booking with ID " + aptId + " deleted successfully";
        } catch (Exception e) {
            return "Error deleting booking with ID " + aptId + ": " + e.getMessage();
        }
    }
}
