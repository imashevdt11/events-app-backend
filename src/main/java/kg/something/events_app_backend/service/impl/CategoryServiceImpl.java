package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.CategoryListDto;
import kg.something.events_app_backend.dto.request.CategoryRequest;
import kg.something.events_app_backend.dto.response.CategoryDetailedResponse;
import kg.something.events_app_backend.dto.response.CategoryResponse;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.CategoryMapper;
import kg.something.events_app_backend.repository.CategoryRepository;
import kg.something.events_app_backend.service.CategoryService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final UserService userService;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository repository, UserService userService, CategoryMapper categoryMapper) {
        this.repository = repository;
        this.userService = userService;
        this.categoryMapper = categoryMapper;
    }

    public String createCategory(CategoryRequest category) {
        User user = userService.getAuthenticatedUser();
        if (repository.existsByName(category.name())) {
            throw new ResourceAlreadyExistsException("Категория с названием '%s' уже есть в базе данных".formatted(category.name()));
        }
        repository.save(new Category(category.name(), user));

        return "Категория создана";
    }

    @Override
    public String deleteCategory(UUID id) {
        Category category = findCategoryById(id);
        if (repository.countEventsByCategory(category.getId()) > 0) {
            throw new InvalidRequestException("Категория не может быть удаленна поскольку привязана к мероприятиям");
        }
        repository.delete(category);

        return "Категория удалена";
    }

    public Category findCategoryById(UUID id) {
        return Optional.ofNullable(repository.findCategoryById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Категория с id '%s' не найдена в базе данных".formatted(id)));
    }

    public Category findCategoryByName(String name) {
        return Optional.ofNullable(repository.findCategoryByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Категория с названием '%s' не найдена в базе данных".formatted(name)));
    }

    public List<CategoryResponse> getAllCategories() {
        return repository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public Integer getAmountOfEventsByCategory(UUID categoryId) {
        return repository.countEventsByCategory(categoryId);
    }

    public List<CategoryListDto> getCategoriesForList() {
        return repository.findAll().stream()
                .map(category ->
                        new CategoryListDto(
                                category.getId(),
                                category.getName(),
                                category.getUser() == null ? "-" : "%s %s".formatted(category.getUser().getFirstName(), category.getUser().getLastName()),
                                category.getCreatedAt(),
                                category.getUpdatedAt(),
                                getAmountOfEventsByCategory(category.getId()))
                )
                .toList();
    }

    public CategoryDetailedResponse getCategoryById(UUID id) {
        Category category = findCategoryById(id);
        return categoryMapper.toCategoryDetailedResponse(category);
    }

    public String updateCategory(CategoryRequest categoryRequest, UUID id) {
        Category category = findCategoryById(id);
        String oldCategoryName = category.getName();
        if (category.getName().equals(categoryRequest.name())) {
            return "Категория не изменена. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByName(categoryRequest.name())) {
            throw new ResourceAlreadyExistsException("Категория с названием '%s' уже есть в базе данных".formatted(category.getName()));
        }
        category.setName(categoryRequest.name());
        repository.save(category);

        return "Название категории изменено с '%s' на '%s'".formatted(oldCategoryName, category.getName());
    }
}
