package com.nicat.rolebasedaccesscontrol.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequestDto {
    @NotEmpty(message = "Username must not be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(columnDefinition = "VARCHAR(255)")
    String username;

    @Size(min = 3, max = 30, message = "Password must be between 3 adn 30 characters")
    @NotEmpty(message = "Password cannot be empty")
    String password;
}
