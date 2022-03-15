package com.example.application.data.service;

import com.example.application.data.entity.CategoryDevice;
import com.example.application.data.repository.CategoryDeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryDeviceService {
    private final CategoryDeviceRepository categoryDeviceRepository;

    public CategoryDeviceService(CategoryDeviceRepository categoryDeviceRepository) {
        this.categoryDeviceRepository = categoryDeviceRepository;
    }

    public List<CategoryDevice> findAllCategoryDevice(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return categoryDeviceRepository.findAll();
        } else {
            return categoryDeviceRepository.search(stringFilter);
        }
    }

    public void deleteCategoryDevice(CategoryDevice categoryDevice) {
        categoryDeviceRepository.delete(categoryDevice);
    }

    public void saveCategoryDevice(CategoryDevice categoryDevice) {
        if (categoryDevice == null) {
            System.err.println("CategoryDevice is null. Are you sure you have connected your form to the application?");
            return;
        }
        categoryDeviceRepository.save(categoryDevice);
    }

    public List<CategoryDevice> findAllCategoryDevices() {
        return categoryDeviceRepository.findAll();
    }

}
