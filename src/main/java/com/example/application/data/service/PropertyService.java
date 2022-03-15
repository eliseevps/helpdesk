package com.example.application.data.service;

import com.example.application.data.entity.CategoryDevice;
import com.example.application.data.entity.Property;
import com.example.application.data.repository.PropertyCategoryDeviceRepository;
import com.example.application.data.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyCategoryDeviceRepository propertyCategoryDeviceRepository;

    public PropertyService(PropertyRepository propertyRepository, PropertyCategoryDeviceRepository propertyCategoryDeviceRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyCategoryDeviceRepository = propertyCategoryDeviceRepository;
    }

    public List<Property> findAllProperty(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return propertyRepository.findAll();
        } else {
            return propertyRepository.search(stringFilter);
        }
    }

    public List<Property> findAllPropertyNotIn(CategoryDevice filter) {
        if (filter == null) {
            return propertyRepository.findAll();
        } else {
            return propertyCategoryDeviceRepository.searchNotIn(filter);
        }
    }

    public void deleteProperty(Property property) {
        propertyRepository.delete(property);
    }


    public void saveProperty(Property property) {
        if (property == null) {
            System.err.println("Property is null. Are you sure you have connected your form to the application?");
            return;
        }
        propertyRepository.save(property);
    }

    public List<Property> findAllPropertys() {
        return propertyRepository.findAll();
    }
}
