package com.example.application.data.service;

import com.example.application.data.entity.Requests;
import com.example.application.data.repository.RequestsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestsService {

    private final RequestsRepository requestsRepository;

    public RequestsService(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    public List<Requests> findAllRequests(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return requestsRepository.findAll();
        } else {
            return requestsRepository.search(stringFilter);
        }
    }

    public void deleteRequests(Requests requests) {
        requestsRepository.delete(requests);
    }

    public Requests saveRequests(Requests requests) {
        return Optional.of(requests)
                .map(requestsRepository::save)
                .get();
    }
}
