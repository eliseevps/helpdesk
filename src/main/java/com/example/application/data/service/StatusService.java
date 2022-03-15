package com.example.application.data.service;

import com.example.application.data.entity.Status;
import com.example.application.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> findAllStatus(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return statusRepository.findAll();
        } else {
            return statusRepository.search(stringFilter);
        }
    }

    public void deleteStatus(Status status) {
        statusRepository.delete(status);
    }

    public void saveStatus(Status status) {
        if (status == null) {
            System.err.println("Status is null. Are you sure you have connected your form to the application?");
            return;
        }
        statusRepository.save(status);
    }

    public List<Status> findAllStatuses() {
        return statusRepository.findAll();
    }

}
