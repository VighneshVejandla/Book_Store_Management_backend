package com.cts.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRoleDto {

    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "user|admin", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Role must be 'user' or 'admin'")
    private String role;
}
