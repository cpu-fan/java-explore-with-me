package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryRequestDto;
import ru.practicum.ewm.dto.category.CategoryResponseDto;
import ru.practicum.ewm.errorhandler.exceptions.ConflictException;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.category.CategoryMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.repository.category.CategoryRepository;
import ru.practicum.ewm.repository.event.EventRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper mapper;

    private final EventRepository eventRepository;

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto categoryDto) {
        Category category = mapper.toCategory(categoryDto);
        category = categoryRepository.save(category);
        log.info("Добавлена новая категория: {}", category);
        return mapper.toDto(category);
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category category = getEntityCategory(categoryId);
        checkExistingEventsForCategory(categoryId);
        categoryRepository.deleteById(categoryId);
        log.info("Удалена категория: {}", category);
    }

    @Override
    public CategoryResponseDto updateCategory(long categoryId, CategoryRequestDto categoryDto) {
        Category category = getEntityCategory(categoryId);
        category = mapper.partialUpdate(categoryDto, category);
        category = categoryRepository.save(category);
        log.info("Обновлена категория: {}", category);
        return mapper.toDto(category);
    }

    @Override
    public Collection<CategoryResponseDto> getCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        Collection<Category> categories = categoryRepository.findAll(page).getContent();
        log.info("Запрошен список категорий");
        return categories.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategory(long categoryId) {
        Category category = getEntityCategory(categoryId);
        log.info("Запрошена категория: {}", category);
        return mapper.toDto(category);
    }

    @Override
    public Category getEntityCategory(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> {
            String message = "Категория id = " + categoryId + " не найдена";
            log.error(message);
            throw new NotFoundException(message);
        });
    }

    private void checkExistingEventsForCategory(long categoryId) {
        if (eventRepository.existsByCategoryId(categoryId)) {
            String message = "У категории categoryId = " + categoryId + " имеются существующие события";
            log.error(message);
            throw new ConflictException(message);
        }
    }
}
