package com.nicat.rolebasedaccesscontrol.controller;

import com.nicat.rolebasedaccesscontrol.model.dto.request.RoleCreateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.request.RoleUpdateDto;
import com.nicat.rolebasedaccesscontrol.model.dto.response.RoleUpdateResponse;
import com.nicat.rolebasedaccesscontrol.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody RoleCreateDto roleCreateDto) {
        roleService.create(roleCreateDto);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public RoleUpdateResponse update(@RequestBody RoleUpdateDto roleUpdateDto) {
        return roleService.update(roleUpdateDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }
}