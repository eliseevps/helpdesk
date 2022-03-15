package com.example.application.data.repository;

import com.example.application.data.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    @Query("select d from Device d " +
            "where lower(d.id) like lower(concat('%', :searchTerm, '%'))")
    List<Device> search(@Param("searchTerm") String searchTerm);
}
