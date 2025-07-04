package com.nicat.rolebasedaccesscontrol.service;

import com.nicat.rolebasedaccesscontrol.dao.entity.Role;
import com.nicat.rolebasedaccesscontrol.dao.repository.RoleRepository;
import com.nicat.rolebasedaccesscontrol.model.dto.request.RoleCreateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.request.RoleUpdateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.response.RoleUpdateResponse;
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
public class RoleService {
    RoleRepository roleRepository;
    UserRole userRole;

    public void create(RoleCreateDto roleCreateDto) {
        log.info("create method was started for RoleService");
        Set<String> currentUserRole = userRole.getCurrentUserRole();
        if (!currentUserRole.contains("MODERATOR")) {
            throw new ForbiddenException("You can not update ,because you are not MODERATOR");
        }
        log.info("create method was started for RoleService");
        log.info("{}", roleCreateDto);
        if (roleRepository.findByName(roleCreateDto.getName().toUpperCase()).isPresent()) {
            throw new AlreadyExistException("This role is exists");
        }
        String roleName = roleCreateDto.getName().toUpperCase();
        Role role = Role.builder()
                .name(roleName)
                .build();
        log.info("new role was successfully created");
        roleRepository.save(role);
        log.info("role was successfully saved in database");
    }

    public RoleUpdateResponse update(RoleUpdateDto roleUpdateDto) {
        log.info("update method was started for RoleService");
        Set<String> roleName = userRole.getCurrentUserRole();
        if (!roleName.contains("ADMIN")) {
            throw new ForbiddenException("You can not update ,because you are not ADMIN");
        }
        String editingNewRoleName = roleUpdateDto.getNewRoleName().toUpperCase();
        String editingOldRoleName = roleUpdateDto.getOldRoleName().toUpperCase();
        Role role = roleRepository.findByName(editingOldRoleName)
                .orElseThrow(() -> new NotFoundException("role name : " + editingOldRoleName + " was not found"));
        log.info("role name : {} was found in database", editingOldRoleName);
        role.setName(editingNewRoleName);
        log.info("new role name : {} set to old role name : {} ", editingNewRoleName, editingOldRoleName);
        roleRepository.save(role);
        log.info("new role name saved with success");
        RoleUpdateResponse roleUpdateResponse = new RoleUpdateResponse();
        roleUpdateResponse.setNewRoleName(editingNewRoleName);
        return roleUpdateResponse;
    }

    public void delete(Long id) {
        log.info("delete method was started for RoleService");
        Set<String> roleName = userRole.getCurrentUserRole();
        if (!roleName.contains("MODERATOR")) {
            throw new ForbiddenException("You can not update ,because you are not MODERATOR");
        }
        log.info("you can delete id for role");
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("roleId : " + id + " was not found"));
        log.info("roleId:{} was found", id);
        roleRepository.deleteById(id);
        log.info("roleId:{} was successfully deleted", id);
        log.info("method was done");
    }
}