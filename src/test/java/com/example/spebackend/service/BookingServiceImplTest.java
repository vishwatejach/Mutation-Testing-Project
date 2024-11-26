package com.example.spebackend.service;

import com.example.spebackend.model.Booking;
import com.example.spebackend.model.Customer;
import com.example.spebackend.repository.BookingRepository;
        import com.example.spebackend.repository.CustomerRepository;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;

        import java.time.LocalDate;
        import java.time.LocalTime;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Optional;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCallHistory() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setDate(LocalDate.now());
        booking.setTime(LocalTime.now());
        booking.setTabletype(2);

        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking savedBooking = bookingService.saveCallHistory(booking);

        assertNotNull(savedBooking);
        assertEquals(1L, savedBooking.getId());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testGetCallHistoryForDoctor_CustomerExists() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(bookingRepository.findByCustomer_Id(customerId)).thenReturn(bookings);

        List<Booking> result = bookingService.getCallHistoryForDoctor(customerId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findById(customerId);
        verify(bookingRepository, times(1)).findByCustomer_Id(customerId);
    }

    @Test
    void testGetCallHistoryForDoctor_CustomerNotExists() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        List<Booking> result = bookingService.getCallHistoryForDoctor(customerId);

        assertNull(result);
        verify(customerRepository, times(1)).findById(customerId);
        verify(bookingRepository, times(0)).findByCustomer_Id(anyLong());
    }

    @Test
    void testDeleteAppointment_BookingExists() {
        Long aptId = 1L;
        Booking booking = new Booking();
        booking.setId(aptId);

        when(bookingRepository.getReferenceById(aptId)).thenReturn(booking);

        String result = bookingService.deleteAppointment(aptId);

        assertEquals("Booking Cancelled Successfully", result);
        verify(bookingRepository, times(1)).getReferenceById(aptId);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testDeleteAppointment_BookingNotExists() {
        Long aptId = 1L;

        when(bookingRepository.getReferenceById(aptId)).thenReturn(null);

        String result = bookingService.deleteAppointment(aptId);

        assertEquals("Unable to Cancel Booking!", result);
        verify(bookingRepository, times(1)).getReferenceById(aptId);
        verify(bookingRepository, times(0)).save(any());
    }
}
