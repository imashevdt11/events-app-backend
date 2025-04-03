package kg.something.events_app_backend.controller;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.entity.Role;
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
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createRole(@Valid @RequestBody RoleDto role) {
        return new ResponseEntity<>(service.createRole(role), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable UUID id) {
        return new ResponseEntity<>(service.deleteRole(id), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(service.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRolesNames() {
        return new ResponseEntity<>(service.getAllRolesNames(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.getRoleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRole(@Valid @RequestBody RoleDto role,
                                                 @PathVariable UUID id) {
        return new ResponseEntity<>(service.updateRole(role, id), HttpStatus.OK);
    }
}
