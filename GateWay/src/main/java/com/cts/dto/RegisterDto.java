package com.cts.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private Long userId;
    private String name;
    private String email;
    @Pattern(message ="Password must have atleast 8 char long with atleast 1 Alphabet, 1 special Char, and 1 Number",
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])(?=.*[^\\s]).{8,}$")
    private String password;
}
