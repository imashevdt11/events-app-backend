package kg.something.events_app_backend.controller.mvc;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.dto.PermissionListDto;
import kg.something.events_app_backend.dto.response.PermissionDetailedResponse;
import kg.something.events_app_backend.service.PermissionService;
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
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService service;

    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @GetMapping("/create-form")
    public String moveToCreatePermissionForm() {
        return "permission_create_form";
    }

    @PostMapping("/create")
    public String createPermission(@Valid @ModelAttribute PermissionDto permission, Model model) {
        try {
            service.createPermission(permission);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось создать право доступа по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/permissions";
    }

    @GetMapping
    public String getPermissions(Model model) {
        try {
            List<PermissionListDto> permissions = service.getPermissionsForList();
            model.addAttribute("permissions", permissions);
            return "permission_list";
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось получить список прав доступа по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePermission(@PathVariable UUID id, Model model) {
        try {
            service.deletePermission(id);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось удалить право доступа по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/permissions";
    }

    @GetMapping("/update/{id}")
    public String moveToEditPermissionForm(@PathVariable UUID id, Model model) {
        PermissionDetailedResponse permission = service.getPermissionById(id);
        model.addAttribute("permission", permission);
        return "permission_edit_form";
    }

    @PostMapping("/update/{id}")
    public String updatePermission(@PathVariable UUID id,
                                   @Valid @ModelAttribute PermissionDto permission,
                                   Model model) {
        try {
            service.updatePermission(permission, id);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось изменить право доступа по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/permissions";
    }
}
