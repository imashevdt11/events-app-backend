package kg.something.events_app_backend.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.response.CategoryDetailedResponse;
import kg.something.events_app_backend.dto.response.CategoryResponse;
import kg.something.events_app_backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/categories")
public class CategoryControllerApi {

    private final CategoryService service;

    public CategoryControllerApi(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "Создание категории")
    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDto category) {
        return new ResponseEntity<>(service.createCategory(category), HttpStatus.CREATED);
    }

    @Operation(summary = "Получение списка всех категорий")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return new ResponseEntity<>(service.getAllCategories(), HttpStatus.OK);
    }

    @Operation(summary = "Получение информации о категории по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailedResponse> getCategory(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.getCategoryById(id), HttpStatus.OK);
    }

    @Operation(summary = "Изменение названия категории")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody CategoryDto category,
                                                 @PathVariable UUID id) {
        return new ResponseEntity<>(service.updateCategory(category, id), HttpStatus.OK);
    }

    @Operation(summary = "Удаление категории")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        return new ResponseEntity<>(service.deleteCategory(id), HttpStatus.OK);
    }
}