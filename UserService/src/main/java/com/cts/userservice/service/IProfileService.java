package com.cts.userservice.service;

import com.cts.userservice.dto.ProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface IProfileService {

    ProfileDto createProfile(ProfileDto profileDto, Long userId, MultipartFile image);

    ProfileDto getProfileByUserId(Long userId);

    ProfileDto updateProfile(Long userId, ProfileDto profileDto, MultipartFile image);

    void deleteProfileByUserId(Long userId);

    void recoverProfile(Long userId);

    void hardDeleteProfile(Long userId);

//
//
//    int calculateProfileCompletion(ProfileDto profileDto);
//
//    void changeVisibility(Long userId, String visibility);
}