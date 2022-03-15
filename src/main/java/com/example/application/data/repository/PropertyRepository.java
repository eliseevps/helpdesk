package com.example.application.data.repository;

import com.example.application.data.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Integer> {
    @Query("select p from Property p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%'))")
    List<Property> search(@Param("searchTerm") String searchTerm);
}