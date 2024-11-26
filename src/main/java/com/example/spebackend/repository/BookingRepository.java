package com.example.spebackend.repository;

import com.example.spebackend.model.Booking;
import com.example.spebackend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRestaurantAndDateAndTime(Restaurant restaurant, LocalDate date, LocalTime time);
    List<Booking> findByCustomer_Id(Long customerId);
}
