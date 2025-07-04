package com.nicat.rolebasedaccesscontrol.model.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionUpdateDto {
    String newPermissionName;
    String oldPermissionName;
}