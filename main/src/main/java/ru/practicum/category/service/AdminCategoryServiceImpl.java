package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCat) {
        Category category = categoryRepository.save(CategoryMapper.toEntity(newCat));
        return CategoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(int categoryId) {
        checkExistence(categoryId);
        checkEventsExist(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(int categoryId, NewCategoryDto categoryDto) {
        Category category = checkExistence(categoryId);
        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }

    private Category checkExistence(int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена или недоступна."));
    }

    private void checkEventsExist(int catId) {
        if (eventRepository.countAllByCategory_Id(catId) > 0) {
            throw new ForbiddenException("Существуют события, связанные с категорией.");
        }
    }
}
