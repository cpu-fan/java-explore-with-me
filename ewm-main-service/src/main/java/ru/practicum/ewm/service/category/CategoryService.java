package ru.practicum.ewm.service.category;

import ru.practicum.ewm.dto.category.CategoryRequestDto;
import ru.practicum.ewm.dto.category.CategoryResponseDto;
import ru.practicum.ewm.model.category.Category;

import java.util.Collection;

public interface CategoryService {

    CategoryResponseDto addCategory(CategoryRequestDto category);

    void deleteCategory(long categoryId);

    CategoryResponseDto updateCategory(long categoryId, CategoryRequestDto category);

    Collection<CategoryResponseDto> getCategories(int from, int size);

    CategoryResponseDto getCategory(long categoryId);

    Category getEntityCategory(long categoryId);
}
