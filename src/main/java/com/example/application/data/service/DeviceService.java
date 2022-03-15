package com.example.application.data.service;

import com.example.application.data.entity.Device;
import com.example.application.data.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    public final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> findAllDevice(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return deviceRepository.findAll();
        } else {
            return deviceRepository.search(stringFilter);
        }
    }

    public void deleteDevice(Device device) {
        deviceRepository.delete(device);
    }

    public void saveDevice(Device device) {
        if (device == null) {
            System.err.println("Device is null. Are you sure you have connected your form to the application?");
            return;
        }
        deviceRepository.save(device);
    }

    public List<Device> findAllDevices() {
        return deviceRepository.findAll();
    }
}
