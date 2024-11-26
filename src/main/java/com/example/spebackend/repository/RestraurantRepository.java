package com.example.spebackend.repository;

import com.example.spebackend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestraurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByTableCountGreaterThan(int tableCount);
}
