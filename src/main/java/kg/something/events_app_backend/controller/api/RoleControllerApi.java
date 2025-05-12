package kg.something.events_app_backend.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.dto.response.RoleDetailedResponse;
import kg.something.events_app_backend.dto.response.RoleResponse;
import kg.something.events_app_backend.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/roles")
public class RoleControllerApi {

    private final RoleService service;

    public RoleControllerApi(RoleService service) {
        this.service = service;
    }

    @Operation(summary = "Создание роли")
    @PostMapping
    public ResponseEntity<String> createRole(@Valid @RequestBody RoleDto role) {
        return new ResponseEntity<>(service.createRole(role), HttpStatus.CREATED);
    }

    @Operation(summary = "Получение списка всех ролей")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return new ResponseEntity<>(service.getAllRoles(), HttpStatus.OK);
    }

    @Operation(summary = "Получение информации о роли по ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDetailedResponse> getRoleById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.getRoleById(id), HttpStatus.OK);
    }

    @Operation(summary = "Изменить информацию о роли")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRole(@Valid @RequestBody RoleDto role,
                                             @PathVariable UUID id) {
        return new ResponseEntity<>(service.updateRole(role, id), HttpStatus.OK);
    }

    @Operation(summary = "Удаление роли")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable UUID id) {
        return new ResponseEntity<>(service.deleteRole(id), HttpStatus.OK);
    }
}
