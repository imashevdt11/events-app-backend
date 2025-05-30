package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.CategoryListDto;
import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.response.CategoryDetailedResponse;
import kg.something.events_app_backend.dto.response.CategoryResponse;
import kg.something.events_app_backend.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    String createCategory(CategoryDto category);

    String deleteCategory(UUID id);

    Category findCategoryByName(String name);

    List<CategoryResponse> getAllCategories();

    Integer getAmountOfEventsByCategory(UUID categoryId);

    List<CategoryListDto> getCategoriesForList();

    CategoryDetailedResponse getCategoryById(UUID id);

    String updateCategory(CategoryDto categoryDto, UUID id);
}
