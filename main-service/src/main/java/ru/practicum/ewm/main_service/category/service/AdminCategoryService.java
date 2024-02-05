package ru.practicum.ewm.main_service.category.service;

import ru.practicum.ewm.main_service.category.dto.CategoryDto;
import ru.practicum.ewm.main_service.category.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto saveCategory(NewCategoryDto categoryDto);

    void deleteCategory(int categoryId);

    CategoryDto updateCategory(int categoryId, NewCategoryDto categoryDto);
}
