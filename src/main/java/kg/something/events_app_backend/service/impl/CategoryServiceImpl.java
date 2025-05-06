package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.CategoryListDto;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.CategoryRepository;
import kg.something.events_app_backend.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    public String createCategory(CategoryDto category) {
        if (repository.existsByName(category.name())) {
            throw new ResourceAlreadyExistsException("Категория с названием '" + category.name() + "' уже есть в базе данных");
        }
        repository.save(new Category(category.name()));

        return "Категория '" + category.name() + "' сохранена";
    }

    @Override
    public String deleteCategory(UUID id) {
        Category category = repository.findCategoryById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Категория с id " + id + " не найдена");
        }
        if (repository.countEventsByCategory(category.getId()) > 0) {
            throw new InvalidRequestException("Категория не может быть удаленна поскольку привязана к мероприятиям");
        }
        repository.delete(category);

        return "Категория '" + category.getName() + "' удалена";
    }

    public Category findCategoryByName(String name) {
        return Optional.ofNullable(repository.findCategoryByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Категория с названием '%s' не найдена в базе данных".formatted(name)));
    }

    public List<CategoryDto> getAllCategoriesNames() {
        return repository.getAllCategoriesNames();
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public List<CategoryListDto> getCategoriesForList() {
        return repository.findAll().stream()
                .map(category ->
                        new CategoryListDto(
                                category.getId(),
                                category.getName(),
                                category.getCreatedAt(),
                                category.getUpdatedAt(),
                                getAmountOfEventsByCategory(category.getId()))
                )
                .toList();
    }

    public Integer getAmountOfEventsByCategory(UUID categoryId) {
        return repository.countEventsByCategory(categoryId);
    }

    public Category getCategoryById(UUID id) {
        Category category = repository.findCategoryById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Категория с id " + id + " не найдена");
        }
        return category;
    }

    public String updateCategory(CategoryDto categoryDto, UUID id) {
        Category category = repository.findCategoryById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Категория с id " + id + " не найдена");
        }

        String oldCategoryName = category.getName();
        if (category.getName().equals(categoryDto.name())) {
            return "Категория не изменена. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByName(categoryDto.name())) {
            throw new ResourceAlreadyExistsException("Категория с названием '" + category.getName() + "' уже есть в базе данных");
        }
        category.setName(categoryDto.name());
        repository.save(category);

        return "Название категории изменено с '" + oldCategoryName + "' на '" + category.getName() + "'";
    }
}
