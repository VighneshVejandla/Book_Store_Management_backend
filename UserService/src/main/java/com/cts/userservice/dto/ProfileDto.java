package com.cts.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    private Long profileId;
    private String bio;
    private String phoneNumber;
    private String address;

    private String profileImageUrl;

//    private Long userId;


//    private String visibility;
//    private int profileCompletion;
//    private boolean isDeleted;
}