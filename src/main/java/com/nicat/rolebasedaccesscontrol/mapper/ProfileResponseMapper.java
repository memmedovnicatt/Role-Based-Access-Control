package com.nicat.rolebasedaccesscontrol.mapper;

import com.nicat.rolebasedaccesscontrol.dao.entity.User;
import com.nicat.rolebasedaccesscontrol.model.dto.response.ProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileResponseMapper {
    ProfileResponse toProfileResponse(User user);
}