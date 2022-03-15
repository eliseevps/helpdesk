package com.example.application.data.repository;

import com.example.application.data.entity.CategoryDevice;
import com.example.application.data.entity.Property;
import com.example.application.data.entity.PropertyCategoryDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyCategoryDeviceRepository extends JpaRepository<PropertyCategoryDevice, Integer> {
    @Query("SELECT p from Property c, PropertyCategoryDevice p " +
            "where p.categoryDevice = :searchTerm and c.id = p.property")
    List<PropertyCategoryDevice> search(@Param("searchTerm") CategoryDevice searchTerm);

    @Query("select p from Property p " +
            "where p.id not in (select c.property from PropertyCategoryDevice c " +
            "where c.categoryDevice= :searchTerm)")
    List<Property> searchNotIn(@Param("searchTerm") CategoryDevice searchTerm);
}
