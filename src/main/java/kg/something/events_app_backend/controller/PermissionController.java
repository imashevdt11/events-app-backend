package kg.something.events_app_backend.controller;

import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.entity.Permission;
import kg.something.events_app_backend.service.PermissionService;
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
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService service;

    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
        return new ResponseEntity<>(service.createPermission(permissionDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePermission(@PathVariable UUID id) {
        return new ResponseEntity<>(service.deletePermission(id), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return new ResponseEntity<>(service.getAllPermissions(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PermissionDto>> getAllPermissionsNames() {
        return new ResponseEntity<>(service.getAllPermissionsNames(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermission(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.getPermissionById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePermission(@Valid @RequestBody PermissionDto permission,
                                                   @PathVariable UUID id) {
        return new ResponseEntity<>(service.updatePermission(permission, id), HttpStatus.OK);
    }
}

