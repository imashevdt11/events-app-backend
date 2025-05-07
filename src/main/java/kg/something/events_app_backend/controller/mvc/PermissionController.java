package kg.something.events_app_backend.controller.mvc;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.dto.PermissionListDto;
import kg.something.events_app_backend.entity.Permission;
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

    @GetMapping
    public String getPermissions(Model model) {
        try {
            System.out.println("asdf1");
            List<PermissionListDto> permissions = service.getPermissionsForList();
            System.out.println("asdf2");
            model.addAttribute("permissions", permissions);
            System.out.println("asdf3");
            return "permission_list";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePermission(@PathVariable UUID id) {
        try {
            service.deletePermission(id);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/permissions";
    }

    @GetMapping("/update/{id}")
    public String editPermission(@PathVariable UUID id, Model model) {
        Permission permission = service.getPermissionById(id);
        model.addAttribute("permission", permission);
        return "permission_edit_form";
    }

    @PostMapping("/update/{id}")
    public String updatePermission(@PathVariable UUID id,
                                   @Valid @ModelAttribute PermissionDto permission) {
        try {
            service.updatePermission(permission, id);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/permissions";
    }
}
