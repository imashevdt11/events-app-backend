package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.dto.CategoryListDto;
import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.response.CategoryDetailedResponse;
import kg.something.events_app_backend.dto.response.CategoryResponse;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.User;
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

    private final CategoryMapper categoryMapper;
    private final CategoryRepository repository;
    private final UserService userService;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository repository, UserService userService) {
        this.categoryMapper = categoryMapper;
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public String createCategory(CategoryDto category) {
        User user = userService.getAuthenticatedUser();
        if (repository.existsByNameIgnoreCase(category.name())) {
            throw new ResourceAlreadyExistsException("Категория с названием '%s' уже есть в базе данных".formatted(category.name()));
        }
        repository.save(new Category(category.name().toUpperCase(), user));

        return "Категория '%s' создана".formatted(category.name());
    }

    @Override
    @Transactional
    public String deleteCategory(UUID id) {
        Category category = findCategoryById(id);
        repository.deleteConnectionsBetweenEventAndCategory(id);
        repository.delete(category);

        return "Категория удалена";
    }

    public Category findCategoryById(UUID id) {
        return Optional.ofNullable(repository.findCategoryById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Категория с id '%s' не найдена в базе данных".formatted(id)));
    }

    @Override
    public Category findCategoryByName(String name) {
        return Optional.ofNullable(repository.findCategoryByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Категория с названием '%s' не найдена в базе данных".formatted(name)));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return repository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public Integer getAmountOfEventsByCategory(UUID categoryId) {
        return repository.countEventsByCategory(categoryId);
    }

    @Override
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

    @Override
    public CategoryDetailedResponse getCategoryById(UUID id) {
        Category category = findCategoryById(id);
        return categoryMapper.toCategoryDetailedResponse(category);
    }

    @Override
    public String updateCategory(CategoryDto categoryDto, UUID id) {
        Category category = findCategoryById(id);
        String oldCategoryName = category.getName();
        if (category.getName().equals(categoryDto.name())) {
            return "Категория не изменена. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByNameIgnoreCase(categoryDto.name())) {
            throw new ResourceAlreadyExistsException("Категория с названием '%s' уже есть в базе данных".formatted(category.getName().toUpperCase()));
        }
        category.setName(categoryDto.name());
        repository.save(category);

        return "Название категории изменено с '%s' на '%s'".formatted(oldCategoryName, category.getName());
    }
}
