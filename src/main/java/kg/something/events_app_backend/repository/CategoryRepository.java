package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

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

    boolean existsByNameIgnoreCase(String name);

    Category findCategoryById(UUID id);

    Category findCategoryByName(String name);
}
