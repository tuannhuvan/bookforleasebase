package com.library.management.mapper;

import com.library.management.dto.request.CategoryForm;
import com.library.management.dto.response.CategoryDTO;
import com.library.management.entity.Category;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
    }

    public static CategoryForm toForm(Category category) {
        if (category == null) return null;
        return CategoryForm.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
    }

    public static Category toEntity(CategoryForm form) {
        if (form == null) return null;
        return Category.builder()
                .name(form.getName())
                .description(form.getDescription())
                .active(form.getActive() != null ? form.getActive() : true)
                .build();
    }
}
