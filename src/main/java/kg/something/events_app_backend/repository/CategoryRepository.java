package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByName(String name);

    Category findCategoryById(UUID id);

    Category findCategoryByName(String name);

    @Query("SELECT new kg.something.events_app_backend.dto.CategoryDto(name) FROM Category")
    List<CategoryDto> getAllCategoriesNames();
}
