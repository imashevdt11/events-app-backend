package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.CategoryListDto;
import kg.something.events_app_backend.dto.request.CategoryRequest;
import kg.something.events_app_backend.dto.response.CategoryResponse;
import kg.something.events_app_backend.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    String createCategory(CategoryRequest category);

    String deleteCategory(UUID id);

    Category findCategoryByName(String name);

    List<CategoryResponse> getAllCategories();

    Category getCategoryById(UUID id);

    String updateCategory(CategoryRequest categoryRequest, UUID id);

    List<CategoryListDto> getCategoriesForList();

    Integer getAmountOfEventsByCategory(UUID categoryId);
}
