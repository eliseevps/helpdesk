package com.example.application.data.service;

import com.example.application.data.entity.CategoryDevice;
import com.example.application.data.entity.PropertyCategoryDevice;
import com.example.application.data.repository.PropertyCategoryDeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyCategoryDeviceService {
    public final PropertyCategoryDeviceRepository propertyCategoryDeviceRepository;

    public PropertyCategoryDeviceService(PropertyCategoryDeviceRepository propertyCategoryDeviceRepository) {
        this.propertyCategoryDeviceRepository = propertyCategoryDeviceRepository;
    }


    public List<PropertyCategoryDevice> findAllPropertyCategoryDevice(CategoryDevice filter) {
        if (filter == null) {
            return propertyCategoryDeviceRepository.findAll();
        } else {
            return propertyCategoryDeviceRepository.search(filter);
        }
    }

    public void deletePropertyCategoryDevice(PropertyCategoryDevice propertyCategoryDevice) {
        propertyCategoryDeviceRepository.delete(propertyCategoryDevice);
    }

    public void savePropertyCategoryDevice(PropertyCategoryDevice propertyCategoryDevice) {
        if (propertyCategoryDevice == null) {
            System.err.println("PropertyCategoryDevice is null. Are you sure you have connected your form to the application?");
            return;
        }
        propertyCategoryDeviceRepository.save(propertyCategoryDevice);
    }
}
