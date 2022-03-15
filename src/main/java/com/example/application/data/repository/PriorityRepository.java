package com.example.application.data.repository;

import com.example.application.data.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    @Query("select p from Priority p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%')) or " +
            "lower(p.description) like lower(concat('%', :searchTerm, '%'))")
    List<Priority> search(@Param("searchTerm") String searchTerm);
}
