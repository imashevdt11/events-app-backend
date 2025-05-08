package kg.something.events_app_backend.controller.mvc;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.dto.RoleListDto;
import kg.something.events_app_backend.entity.Role;
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

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping
    public String getRoles(Model model) {
        try {
            List<RoleListDto> roles = service.getRolesForList();
            model.addAttribute("roles", roles);
            return "role_list";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable UUID id) {
        try {
            service.deleteRole(id);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/roles";
    }

    @GetMapping("/update/{id}")
    public String editRole(@PathVariable UUID id, Model model) {
        Role role = service.getRoleById(id);
        model.addAttribute("role", role);
        return "role_edit_form";
    }

    @PostMapping("/update/{id}")
    public String updateRole(@PathVariable UUID id,
                             @Valid @ModelAttribute RoleDto role) {
        try {
            service.updateRole(role, id);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/roles";
    }
}
