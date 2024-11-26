package com.example.spebackend.controller;

import com.example.spebackend.model.Booking;
import com.example.spebackend.model.Restaurant;
import com.example.spebackend.repository.BookingRepository;
import com.example.spebackend.repository.RestraurantRepository;
import com.example.spebackend.service.BookingService;
import com.example.spebackend.service.RestraurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestraurantControllerTest {
    @Mock
    private RestraurantService restaurantService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RestraurantRepository restraurantRepository;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private RestraurantController restrauntController;

    @Test
    void getAllRestaurantsWithTableCountGreaterThanZero() {
        Restaurant restaurant = new Restaurant();
        List<Restaurant> restaurants = Collections.singletonList(restaurant);
        when(restaurantService.getAllRestaurantsWithTableCountGreaterThanZero()).thenReturn(restaurants);

        // Call the method under test
        List<Restaurant> result = restrauntController.getAllRestaurantsWithTableCountGreaterThanZero();

        // Assertions
        assertEquals(restaurants, result);
        verify(restaurantService, times(1)).getAllRestaurantsWithTableCountGreaterThanZero();
    }

    @Test
    void getAvailableTables() {
        Long resId = 1L;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Mocking repository response
        Restaurant restaurant = new Restaurant();
        when(restraurantRepository.findById(resId)).thenReturn(Optional.of(restaurant));
        List<Booking> bookings = Collections.emptyList();
        when(bookingRepository.findByRestaurantAndDateAndTime(restaurant, date, time)).thenReturn(bookings);

        // Call the method under test
        List<Booking> result = restrauntController.getAvailableTables(resId, date, time);

        // Assertions
        assertEquals(bookings, result);
        verify(restraurantRepository, times(1)).findById(resId);
        verify(bookingRepository, times(1)).findByRestaurantAndDateAndTime(restaurant, date, time);
    }

    @Test
    void bookApt() {
        Booking booking = new Booking();
        // Set booking properties

        // Mocking service response
        when(bookingService.saveCallHistory(booking)).thenReturn(null);

        // Call the method under test
        ResponseEntity<String> result = restrauntController.bookApt(booking);

        // Assertions
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Table booked successfully", result.getBody());
        verify(bookingService, times(1)).saveCallHistory(booking);
    }
}