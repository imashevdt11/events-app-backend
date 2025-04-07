package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.entity.Category;
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
        if (repository.existsByName(category.getName())) {
            throw new ResourceAlreadyExistsException("Категория с названием '" + category.getName() + "' уже есть в базе данных");
        }
        repository.save(new Category(category.getName()));

        return "Категория '" + category.getName() + "' сохранена";
    }

    @Override
    public String deleteCategory(UUID id) {
        Category category = repository.findCategoryById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Категория с id " + id + " не найдена");
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
        if (category.getName().equals(categoryDto.getName())) {
            return "Категория не изменена. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByName(categoryDto.getName())) {
            throw new ResourceAlreadyExistsException("Категория с названием '" + category.getName() + "' уже есть в базе данных");
        }
        category.setName(categoryDto.getName());
        repository.save(category);

        return "Название категории изменено с '" + oldCategoryName + "' на '" + category.getName() + "'";
    }
}
