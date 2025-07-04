package com.nicat.rolebasedaccesscontrol.service;


import com.nicat.rolebasedaccesscontrol.dao.entity.Permission;
import com.nicat.rolebasedaccesscontrol.dao.entity.Role;
import com.nicat.rolebasedaccesscontrol.dao.repository.PermissionRepository;
import com.nicat.rolebasedaccesscontrol.dao.repository.RoleRepository;
import com.nicat.rolebasedaccesscontrol.model.dto.request.PermissionRoleRequestDto;
import com.nicat.rolebasedaccesscontrol.model.exception.ForbiddenException;
import com.nicat.rolebasedaccesscontrol.model.exception.NotFoundException;
import com.nicat.rolebasedaccesscontrol.util.UserRole;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RolePermissionService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    UserRole userRole;

    public void assignToUser(PermissionRoleRequestDto permissionRoleRequestDto) {
        Set<String> roleName = userRole.getCurrentUserRole();
        if (!roleName.contains("MODERATOR")) {
            throw new ForbiddenException("You can not assign permission to user");
        }
        log.info("confirmed of user's role");

        String editingPermissionName = permissionRoleRequestDto.getPermissionName().toUpperCase();
        log.info("editingPermissionName from '{}' | to: '{}'", permissionRoleRequestDto.getPermissionName(), editingPermissionName);

        Permission permission = permissionRepository.findByName(editingPermissionName)
                .orElseThrow(() -> new NotFoundException
                        ("permission: " + permissionRoleRequestDto.getPermissionName() + " is not found"));
        log.info("permission was found");

        String editingRoleName = permissionRoleRequestDto.getRoleName().toUpperCase();
        log.info("role name was edited from : '{}', |  to: '{}' ", permissionRoleRequestDto.getRoleName(), editingRoleName);

        Role role = roleRepository.findByName(editingRoleName)
                .orElseThrow(() -> new NotFoundException
                        ("role " + permissionRoleRequestDto.getRoleName() + " is not found"));
        log.info("role was found");
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }
}