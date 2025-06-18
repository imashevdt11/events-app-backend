package kg.something.events_app_backend.controller.mvc;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.UserListDto;
import kg.something.events_app_backend.dto.request.UserUpdateRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.RoleResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.service.AuthService;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final AuthService authService;
    private final RoleService roleService;
    private final UserService userService;

    public UserController(AuthService authService, RoleService roleService, UserService userService) {
        this.authService = authService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/create-form")
    public String moveToCreateUserForm(Model model) {
        List<RoleResponse> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "user_create_form";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute UserRegistrationRequest user, Model model) {
        try {
            authService.register(user);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось создать пользователя по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/users";
    }

    @GetMapping
    public String getAllUsers(Model model) {
        try {
            List<UserListDto> users = userService.getUsersForList();
            List<RoleResponse> roles = roleService.getAllRoles();
            model.addAttribute("users", users);
            model.addAttribute("roles", roles);
            return "user_list";
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось получить список пользователей по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
    }

    @GetMapping("/change-role/{id}")
    public String changeUserRole(@PathVariable UUID id,
                                 @RequestParam("role") String role,
                                 Model model) {
        try {
            userService.changeUserRole(id, role);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось изменить роль пользователя по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/users";
    }

    @GetMapping("/change-status/{id}")
    public String changeUserStatus(@PathVariable UUID id, Model model) {
        try {
            userService.changeUserStatus(id);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось изменить статус пользователя по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/users";
    }

    @GetMapping("/update/{id}")
    public String moveToUpdateUserForm(@PathVariable UUID id, Model model) {
        UserResponse user = userService.getUserById(id);
        List<RoleResponse> roles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "user_edit_form";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable UUID id,
                             @Valid @ModelAttribute UserUpdateRequest user,
                             Model model) {
        try {
            userService.updateUser(id, user);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось изменить данные пользователя по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/users";
    }
}
