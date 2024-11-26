package com.example.spebackend.service;

import com.example.spebackend.model.Booking;

import java.util.List;

public interface BookingService {
    public Booking saveCallHistory(Booking callHistory);
    List<Booking> getCallHistoryForDoctor(Long customerId);
    String deleteAppointment(Long aptId);
}
