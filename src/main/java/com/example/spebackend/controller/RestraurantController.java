package com.example.spebackend.controller;

import com.example.spebackend.model.Booking;
import com.example.spebackend.model.Restaurant;
import com.example.spebackend.repository.BookingRepository;
import com.example.spebackend.repository.RestraurantRepository;
import com.example.spebackend.service.BookingService;
import com.example.spebackend.service.RestraurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurants")
public class RestraurantController {


    private final RestraurantService restaurantService;
    private final BookingRepository bookingRepository;
    private final RestraurantRepository restraurantRepository;
    private final BookingService bookingService;

    public RestraurantController(RestraurantService restaurantService, BookingRepository bookingRepository, RestraurantRepository restraurantRepository, BookingService bookingService) {
        this.restaurantService = restaurantService;
        this.bookingRepository = bookingRepository;
        this.restraurantRepository = restraurantRepository;
        this.bookingService = bookingService;
    }

    @GetMapping("/tablecountgtzero")
    public List<Restaurant> getAllRestaurantsWithTableCountGreaterThanZero() {
        return restaurantService.getAllRestaurantsWithTableCountGreaterThanZero();
    }

    @GetMapping("/getavailabletables")
    public List<Booking> getAvailableTables(
            @RequestParam Long resId,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time
    ) {
        System.out.println("Inside get available tables");
        Optional<Restaurant> restaurantOptional = restraurantRepository.findById(resId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            return bookingRepository.findByRestaurantAndDateAndTime(restaurant, date, time);
        } else {
            throw new IllegalArgumentException("Restaurant with ID " + resId + " not found");
        }
    }

    @PostMapping("/book-apt")
    public ResponseEntity<String> bookApt(@RequestBody Booking callHistory) {
        Long id = callHistory.getId();
        LocalDate date = callHistory.getDate();
        LocalTime time = callHistory.getTime();
        int tabletype = callHistory.getTabletype();

            try {
                // Add the key to the global map


                // Other logic for booking the appointment...
                callHistory.setTabletype(tabletype);
                bookingService.saveCallHistory(callHistory);
                return ResponseEntity.ok("Table booked successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error booking Appointment");
            }

    }

    @GetMapping("/download/{restImage:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String restImage) {
        try {
            // Load file as Resource
            Resource resource = restaurantService.loadFileAsResource(restImage);

            // Determine content type
            String contentType = URLConnection.guessContentTypeFromName(resource.getFilename());

            // If content type could not be determined, set it to "application/octet-stream"
            if(contentType == null) {
                contentType = "application/octet-stream";
            }

            // Prepare header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());

            // Convert file to byte array
            byte[] fileAsByteArray = StreamUtils.copyToByteArray(resource.getInputStream());

            return new ResponseEntity<>(fileAsByteArray, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
