package com.example.application.data.repository;

import com.example.application.data.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query("select d from Department d " +
            "where lower(d.name) like lower(concat('%', :searchTerm, '%'))")
    List<Department> search(@Param("searchTerm") String searchTerm);
}
