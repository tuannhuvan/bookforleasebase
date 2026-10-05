package com.library.management.service.impl;

import com.library.management.dto.request.CategoryForm;
import com.library.management.dto.response.CategoryDTO;
import com.library.management.entity.Category;
import com.library.management.exception.BusinessRuleException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.mapper.CategoryMapper;
import com.library.management.repository.CategoryRepository;
import com.library.management.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> findActiveCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thể loại với ID: " + id));
        return CategoryMapper.toDTO(category);
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryForm form) {
        if (categoryRepository.existsByName(form.getName().trim())) {
            throw new BusinessRuleException("Tên thể loại '" + form.getName() + "' đã tồn tại!");
        }
        Category category = CategoryMapper.toEntity(form);
        category.setName(form.getName().trim());
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryForm form) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thể loại với ID: " + id));

        if (categoryRepository.existsByNameAndIdNot(form.getName().trim(), id)) {
            throw new BusinessRuleException("Tên thể loại '" + form.getName() + "' đã tồn tại!");
        }

        category.setName(form.getName().trim());
        category.setDescription(form.getDescription());
        if (form.getActive() != null) {
            category.setActive(form.getActive());
        }
        Category updated = categoryRepository.save(category);
        return CategoryMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thể loại với ID: " + id));
        category.setActive(false);
        categoryRepository.save(category);
    }
}
