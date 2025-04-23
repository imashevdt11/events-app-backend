package kg.something.events_app_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "Создание категории")
    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDto category) {
        return new ResponseEntity<>(service.createCategory(category), HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление категории")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        return new ResponseEntity<>(service.deleteCategory(id), HttpStatus.OK);
    }

    @Operation(summary = "Получение списка категорий")
    @GetMapping("/admin")
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(service.getAllCategories(), HttpStatus.OK);
    }

    @Operation(summary = "Получение списка названий категорий")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategoriesNames() {
        return new ResponseEntity<>(service.getAllCategoriesNames(), HttpStatus.OK);
    }

    @Operation(summary = "Получение информации о категории по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.getCategoryById(id), HttpStatus.OK);
    }

    @Operation(summary = "Изменение информации о категории")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody CategoryDto category,
                                                 @PathVariable UUID id) {
        return new ResponseEntity<>(service.updateCategory(category, id), HttpStatus.OK);
    }
}