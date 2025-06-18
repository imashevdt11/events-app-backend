package kg.something.events_app_backend.controller.mvc;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.dto.RoleListDto;
import kg.something.events_app_backend.dto.response.PermissionResponse;
import kg.something.events_app_backend.dto.response.RoleDetailedResponse;
import kg.something.events_app_backend.service.PermissionService;
import kg.something.events_app_backend.service.RoleService;
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
@RequestMapping("/roles")
public class RoleController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    public RoleController(PermissionService permissionService, RoleService roleService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    @GetMapping("/create-form")
    public String moveToCreateRoleForm(Model model) {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        model.addAttribute("permissions", permissions);
        return "role_create_form";
    }

    @PostMapping("/create")
    public String createRole(@Valid @ModelAttribute RoleDto role, Model model) {
        try {
            roleService.createRole(role);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось создать роль по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/roles";
    }

    @GetMapping
    public String getRoles(Model model) {
        try {
            List<RoleListDto> roles = roleService.getRolesForList();
            model.addAttribute("roles", roles);
            return "role_list";
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось получить список ролей по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable UUID id, Model model) {
        try {
            roleService.deleteRole(id);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось удалить роль по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/roles";
    }

    @GetMapping("/update/{id}")
    public String editRole(@PathVariable UUID id, Model model) {
        RoleDetailedResponse role = roleService.getRoleById(id);
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        model.addAttribute("role", role);
        model.addAttribute("permissions", permissions);
        return "role_edit_form";
    }

    @PostMapping("/update/{id}")
    public String updateRole(@PathVariable UUID id,
                             @Valid @ModelAttribute RoleDto role,
                             Model model) {
        try {
            roleService.updateRole(role, id);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось изменить роль по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/roles";
    }
}
