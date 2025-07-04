package com.nicat.rolebasedaccesscontrol.dao.repository;

import com.nicat.rolebasedaccesscontrol.dao.entity.Permission;
import com.nicat.rolebasedaccesscontrol.dao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);

}
