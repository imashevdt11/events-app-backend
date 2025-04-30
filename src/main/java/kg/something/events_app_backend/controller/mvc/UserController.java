package kg.something.events_app_backend.controller.mvc;

import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admins")
    public String getAllAdmins(Model model) {
        try {
            List<UserResponse> admins = userService.getAllUsers();
            model.addAttribute("users", admins);
            return "admin_list";
        } catch (Exception e) {
            return "error";
        }
    }
}
