package com.nicat.rolebasedaccesscontrol.controller;

import com.nicat.rolebasedaccesscontrol.model.dto.request.PermissionRoleRequestDto;
import com.nicat.rolebasedaccesscontrol.service.RolePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RolePermissionController {

    RolePermissionService rolePermissionService;

    @PostMapping("/assign-to-user")
    public void assignToUser(@RequestBody PermissionRoleRequestDto permissionRoleRequestDto) {
        rolePermissionService.assignToUser(permissionRoleRequestDto);
    }
}