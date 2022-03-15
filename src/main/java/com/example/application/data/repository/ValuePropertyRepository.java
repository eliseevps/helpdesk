package com.example.application.data.repository;

import com.example.application.data.entity.Device;
import com.example.application.data.entity.ValueProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ValuePropertyRepository extends JpaRepository<ValueProperty, Integer> {

    @Query("select v from ValueProperty v " +
            "where v.device = :searchTerm ")
    List<ValueProperty> search(@Param("searchTerm") Device searchTerm);
}
