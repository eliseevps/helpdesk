package com.example.application.data.repository;

import com.example.application.data.entity.CategoryDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryDeviceRepository extends JpaRepository<CategoryDevice, Integer> {
    @Query("select c from CategoryDevice c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) or " +
            "lower(c.description) like lower(concat('%', :searchTerm, '%'))")
    List<CategoryDevice> search(@Param("searchTerm") String searchTerm);
}
