package com.example.spebackend.service;

import com.example.spebackend.model.Booking;
import com.example.spebackend.model.Customer;
import com.example.spebackend.repository.BookingRepository;
import com.example.spebackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, CustomerRepository customerRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Booking saveCallHistory(Booking callHistory) {
        return bookingRepository.save(callHistory);
    }

    @Override
    public List<Booking> getCallHistoryForDoctor(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()) {
            return bookingRepository.findByCustomer_Id(customerId);
        }
        else {
            return null;
        }
    }

    @Override
    public String deleteAppointment(Long aptId){
        Booking callapt =  bookingRepository.getReferenceById(aptId);
        if(callapt != null)
        {

            bookingRepository.save(callapt);
            return ("Booking Cancelled Successfully");
        }
        return ("Unable to Cancel Booking!");
    }
}
