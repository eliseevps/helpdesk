package com.example.application.data.service;

import com.example.application.data.entity.Device;
import com.example.application.data.entity.ValueProperty;
import com.example.application.data.repository.ValuePropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValuePropertyService {

    private final ValuePropertyRepository valuePropertyRepository;

    public ValuePropertyService(ValuePropertyRepository valuePropertyRepository) {
        this.valuePropertyRepository = valuePropertyRepository;
    }

    public List<ValueProperty> findAllDevicesValue(Device filter) {
        if (filter == null) {
            return valuePropertyRepository.findAll();
        } else {
            return valuePropertyRepository.search(filter);
        }
    }

    public void deleteValueProperty(ValueProperty valueProperty) {
        valuePropertyRepository.delete(valueProperty);
    }

    public void saveValueProperty(ValueProperty valueProperty) {
        if (valueProperty == null) {
            System.err.println("ValueProperty is null. Are you sure you have connected your form to the application?");
            return;
        }
        valuePropertyRepository.save(valueProperty);
    }

    public List<ValueProperty> findAllValuePropertys() {
        return valuePropertyRepository.findAll();
    }

}
