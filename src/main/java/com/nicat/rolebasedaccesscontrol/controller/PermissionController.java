package com.nicat.rolebasedaccesscontrol.controller;

import com.nicat.rolebasedaccesscontrol.model.dto.request.PermissionCreateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.request.PermissionUpdateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.response.PermissionUpdateResponse;
import com.nicat.rolebasedaccesscontrol.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody PermissionCreateDto permissionCreateDto) {
        permissionService.create(permissionCreateDto);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public PermissionUpdateResponse update(@RequestBody PermissionUpdateDto permissionUpdateDto) {
        return permissionService.update(permissionUpdateDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        permissionService.delete(id);
    }
}