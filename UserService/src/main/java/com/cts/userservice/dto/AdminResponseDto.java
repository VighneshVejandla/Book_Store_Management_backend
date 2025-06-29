package com.cts.userservice.dto;

import com.cts.userservice.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDto {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Profile profile;
}
