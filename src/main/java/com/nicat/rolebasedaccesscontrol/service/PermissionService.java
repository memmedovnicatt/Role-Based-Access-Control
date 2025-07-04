package com.nicat.rolebasedaccesscontrol.service;

import com.nicat.rolebasedaccesscontrol.dao.entity.Permission;
import com.nicat.rolebasedaccesscontrol.dao.repository.PermissionRepository;
import com.nicat.rolebasedaccesscontrol.model.dto.request.PermissionCreateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.request.PermissionUpdateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.response.PermissionUpdateResponse;
import com.nicat.rolebasedaccesscontrol.model.exception.AlreadyExistException;
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
public class PermissionService {

    PermissionRepository permissionRepository;
    UserRole userRole;

    public void create(PermissionCreateDto permissionCreateDto) {
        log.info("create method was started for PermissionService");
        Set<String> roleName = userRole.getCurrentUserRole();
        if (!roleName.contains("MODERATOR")) {
            throw new ForbiddenException("You can not update something,because you are not MODERATOR," +
                    "your roleName is : " + roleName);
        }
        log.info("create method was started for PermissionService");
        if (permissionRepository.findByName(permissionCreateDto.getName().toUpperCase()).isPresent()) {
            throw new AlreadyExistException("This permission is exists");
        }
        String permissionName = permissionCreateDto.getName().toUpperCase();
        Permission permission = Permission.builder()
                .name(permissionName)
                .build();
        log.info("new permission was successfully created");
        permissionRepository.save(permission);
        log.info("role was successfully saved in database");
        log.info("method was done...");
    }

    public PermissionUpdateResponse update(PermissionUpdateDto permissionUpdateDto) {
        log.info("update method was started for PermissionService");
        Set<String> roleName = userRole.getCurrentUserRole();
        if (!roleName.contains("MODERATOR")) {
            throw new ForbiddenException("You can not update something,because you are not MODERATOR," +
                    "your roleName is : " + roleName);
        }
        String editingNewPermissionName = permissionUpdateDto.getNewPermissionName().toUpperCase();
        String editingOldPermissionName = permissionUpdateDto.getOldPermissionName().toUpperCase();
        Permission permission = permissionRepository.findByName(editingOldPermissionName)
                .orElseThrow(() -> new NotFoundException("This permission was not found in database"));
        log.info("permission was found");
        permission.setName(editingNewPermissionName);
        log.info("new permission '{}' was successfully set to old permission '{}'",
                editingNewPermissionName, editingOldPermissionName);
        permissionRepository.save(permission);
        log.info("new permission saved in database");
        PermissionUpdateResponse permissionUpdateResponse = new PermissionUpdateResponse();
        permissionUpdateResponse.setNewPermissionName(permission.getName());
        log.info("Method was done");
        return permissionUpdateResponse;
    }

    public void delete(Long id) {
        log.info("delete method was started for PermissionService");
        Set<String> roleName = userRole.getCurrentUserRole();
        if (!roleName.contains("MODERATOR")) {
            throw new ForbiddenException("You can not update ,because you are not MODERATOR");
        }
        log.info("you can delete id for permission");
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("permissionId : " + id + " was not found"));
        log.info("permissionId:{} was found", id);
        permissionRepository.deleteById(id);
        log.info("permissionId:{} was successfully deleted", id);
        log.info("method was done");
    }
}