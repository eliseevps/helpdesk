package com.example.application.data.service;

import com.example.application.data.entity.Priority;
import com.example.application.data.repository.PriorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public List<Priority> findAllPriority(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return priorityRepository.findAll();
        } else {
            return priorityRepository.search(stringFilter);
        }
    }

    public void deletePriority(Priority priority) {
        priorityRepository.delete(priority);
    }

    public void savePriority(Priority priority) {
        if (priority == null) {
            System.err.println("Priopity is null. Are you sure you have connected your form to the application?");
            return;
        }
        priorityRepository.save(priority);
    }

    public List<Priority> findAllPrioritys() {
        return priorityRepository.findAll();
    }
}
