package com.nicat.rolebasedaccesscontrol.model.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateResponse {
    String newRoleName;
}