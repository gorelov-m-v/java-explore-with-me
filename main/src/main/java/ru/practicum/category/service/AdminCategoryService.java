package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto saveCategory(NewCategoryDto categoryDto);

    void deleteCategory(int categoryId);

    CategoryDto updateCategory(int categoryId, NewCategoryDto categoryDto);
}
