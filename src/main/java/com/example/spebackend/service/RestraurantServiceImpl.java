package com.example.spebackend.service;

import com.example.spebackend.model.Restaurant;
import com.example.spebackend.repository.RestraurantRepository;
import com.example.spebackend.service.RestraurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RestraurantServiceImpl implements RestraurantService {
    private final RestraurantRepository restraurantRepository;
    @Value("${project.image}")
    private String path;

    public RestraurantServiceImpl(RestraurantRepository restraurantRepository) {
        this.restraurantRepository = restraurantRepository;
    }

    @Override
    public List<Restaurant> getAllRestaurantsWithTableCountGreaterThanZero() {
        return restraurantRepository.findByTableCountGreaterThan(0);
    }

    @Override
    public Resource loadFileAsResource(String restImage) throws FileNotFoundException {
        try {
            Path filePath = Paths.get(path).resolve(restImage).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(Files.exists(filePath)) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + restImage);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found: " + restImage);
        }
    }
}