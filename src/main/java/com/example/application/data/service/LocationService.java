package com.example.application.data.service;

import com.example.application.data.entity.Location;
import com.example.application.data.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAllLocation(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return locationRepository.findAll();
        } else {
            return locationRepository.search(stringFilter);
        }
    }

    public void deleteLocation(Location location) {
        locationRepository.delete(location);
    }

    public void saveLocation(Location location) {
        if (location == null) {
            System.err.println("Location is null. Are you sure you have connected your form to the application?");
            return;
        }
        locationRepository.save(location);
    }

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }
}
