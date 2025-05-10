package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.response.CategoryDetailedResponse;
import kg.something.events_app_backend.dto.response.CategoryResponse;
import kg.something.events_app_backend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getUser() == null ? "-" : "%s %s"
                        .formatted(category.getUser().getFirstName(), category.getUser().getLastName())
        );
    }

    public CategoryDetailedResponse toCategoryDetailedResponse(Category category) {
        return new CategoryDetailedResponse(
                category.getId(),
                category.getName(),
                category.getUser() == null ? "-" : "%s %s"
                        .formatted(category.getUser().getFirstName(), category.getUser().getLastName()),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
