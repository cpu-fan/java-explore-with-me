package ru.practicum.ewm.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryRequestDto;
import ru.practicum.ewm.dto.category.CategoryResponseDto;
import ru.practicum.ewm.service.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto addCategory(@Valid @RequestBody CategoryRequestDto category) {
        return categoryService.addCategory(category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryResponseDto updateCategory(@PathVariable long catId,
                                              @Valid @RequestBody CategoryRequestDto category) {
        return categoryService.updateCategory(catId, category);
    }
}
