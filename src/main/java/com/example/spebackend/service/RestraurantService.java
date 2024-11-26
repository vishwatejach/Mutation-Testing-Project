package com.example.spebackend.service;

import com.example.spebackend.model.Restaurant;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.List;

public interface RestraurantService {
    List<Restaurant> getAllRestaurantsWithTableCountGreaterThanZero();

    Resource loadFileAsResource(String filename) throws FileNotFoundException;
}
