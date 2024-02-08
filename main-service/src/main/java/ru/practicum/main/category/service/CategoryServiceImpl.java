package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.NewCategoryDto;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException(String.format("Can't create category because name: %s already used by another category", newCategoryDto.getName()));
        }
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return categoryMapper.toCategoryDtoList(categoryRepository.findAll(page).toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category doesn't exist"));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category doesn't exist"));
        if (!categoryDto.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(categoryDto.getName())) {
                throw new ConflictException(String.format("Can't update category because name: %s already used by another category", categoryDto.getName()));
            }
            category.setName(categoryDto.getName());
        }
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }
}