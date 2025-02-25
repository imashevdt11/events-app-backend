package kg.something.events_app_backend.controller;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.request.RoleRegistrationDto;
import kg.something.events_app_backend.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<?> createRole(
            @Valid @RequestBody RoleRegistrationDto request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + result.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRole(request));
    }
}
