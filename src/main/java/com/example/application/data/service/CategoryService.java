package com.example.application.data.service;

import com.example.application.data.entity.Category;
import com.example.application.data.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategory(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.search(stringFilter);
        }
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public void saveCategory(Category category) {
        if (category == null) {
            System.err.println("Category is null. Are you sure you have connected your form to the application?");
            return;
        }
        categoryRepository.save(category);
    }

    public List<Category> findAllCategorys() {
        return categoryRepository.findAll();
    }
}
