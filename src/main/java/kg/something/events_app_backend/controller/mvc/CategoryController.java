package kg.something.events_app_backend.controller.mvc;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.CategoryListDto;
import kg.something.events_app_backend.entity.Category;
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

    @GetMapping
    public String getCategories(Model model) {
        try {
            List<CategoryListDto> categories = service.getCategoriesForList();
            model.addAttribute("categories", categories);
            return "category_list";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        try {
            service.deleteCategory(id);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/categories";
    }

    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable UUID id, Model model) {
        Category category = service.getCategoryById(id);
        model.addAttribute("category", category);
        return "category_edit_form";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable UUID id, @Valid @ModelAttribute CategoryDto category) {
        try {
            service.updateCategory(category, id);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/categories";
    }
}
