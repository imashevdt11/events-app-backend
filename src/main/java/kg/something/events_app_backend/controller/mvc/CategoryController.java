package kg.something.events_app_backend.controller.mvc;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.CategoryListDto;
import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.response.CategoryDetailedResponse;
import kg.something.events_app_backend.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/create-form")
    public String moveToCreateCategoryForm() {
        return "category_create_form";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute CategoryDto category, Model model) {
        try {
            service.createCategory(category);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось создать категорию по причине: \n%s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/categories";
    }

    @GetMapping
    public String getCategories(Model model) {
        try {
            List<CategoryListDto> categories = service.getCategoriesForList();
            model.addAttribute("categories", categories);
            return "category_list";
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось получить список категорий по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable UUID id, Model model) {
        try {
            service.deleteCategory(id);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось удалить категорию по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/categories";
    }

    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable UUID id, Model model) {
        CategoryDetailedResponse category = service.getCategoryById(id);
        model.addAttribute("category", category);
        return "category_edit_form";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable UUID id, @Valid @ModelAttribute CategoryDto category, Model model) {
        try {
            service.updateCategory(category, id);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось изменить категорию по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/categories";
    }
}
