package com.example.application.data.service;

import com.example.application.data.entity.Department;
import com.example.application.data.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAllDepartment(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return departmentRepository.findAll();
        } else {
            return departmentRepository.search(stringFilter);
        }
    }

    public void deleteDepartment(Department department) {
        departmentRepository.delete(department);
    }

    public void saveDepartment(Department department) {
        if (department == null) {
            System.err.println("Department is null. Are you sure you have connected your form to the application?");
            return;
        }
        departmentRepository.save(department);
    }

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }
}
