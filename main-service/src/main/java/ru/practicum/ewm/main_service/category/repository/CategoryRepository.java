package ru.practicum.ewm.main_service.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main_service.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}