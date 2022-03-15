package com.example.application.data.repository;

import com.example.application.data.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    @Query("select l from Location l " +
            "where lower(l.office) like lower(concat('%', :searchTerm, '%'))")
    List<Location> search(@Param("searchTerm") String searchTerm);
}
