package com.example.application.data.repository;

import com.example.application.data.entity.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestsRepository extends JpaRepository<Requests, Long> {
    @Query("select r from Requests r " +
            "where lower(r.nameRequest) like lower(concat('%', :searchTerm, '%'))")
    List<Requests> search(@Param("searchTerm") String searchTerm);
}
