package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);

    Category findCategoryById(UUID id);

    Category findCategoryByName(String name);

    @Query(value = """
    SELECT COUNT(*)
    FROM events_categories
    WHERE category_id = :categoryId
    """, nativeQuery = true)
    Integer countEventsByCategory(@Param("categoryId") UUID categoryId);

    @Modifying
    @Query(value = """
    DELETE FROM events_categories
    WHERE category_id = :categoryId
    """, nativeQuery = true)
    void deleteConnectionsBetweenEventAndCategory(@Param("categoryId") UUID categoryId);
}
