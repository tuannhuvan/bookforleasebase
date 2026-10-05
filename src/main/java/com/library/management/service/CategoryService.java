package com.library.management.service;

import com.library.management.dto.request.CategoryForm;
import com.library.management.dto.response.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
    List<CategoryDTO> findActiveCategories();
    CategoryDTO findById(Long id);
    CategoryDTO createCategory(CategoryForm form);
    CategoryDTO updateCategory(Long id, CategoryForm form);
    void deleteCategory(Long id); // Soft delete
}
