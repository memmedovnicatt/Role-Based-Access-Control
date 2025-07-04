package com.nicat.rolebasedaccesscontrol.util;

import com.nicat.rolebasedaccesscontrol.dao.entity.User;
import com.nicat.rolebasedaccesscontrol.dao.entity.Role;
import com.nicat.rolebasedaccesscontrol.dao.repository.UserRepository;
import com.nicat.rolebasedaccesscontrol.model.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserRole {
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    public Set<String> getCurrentUserRole() {
        log.info("assignToUser method was started for RolePermissionService");
        String currentUsername = securityUtil.getCurrentUsername();
        if (currentUsername == null || currentUsername.isEmpty()) {
            log.error("Username is null or empty");
            throw new NotFoundException("Username is null or empty");
        }
        log.info("current username is available in system");
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NotFoundException
                        ("This user is not found with this username: " + currentUsername));
        log.info("current username was found in database");
        Set<String> roleName = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        log.info("role of user is : {}", roleName);
        return roleName;
    }
}