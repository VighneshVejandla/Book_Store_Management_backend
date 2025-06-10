package com.cts.userservice.service;

import com.cts.userservice.dto.ProfileDto;

public interface IProfileService {

    ProfileDto createProfile(ProfileDto profileDto, Long userId);

    ProfileDto getProfileByUserId(Long userId);

    ProfileDto updateProfile(Long userId, ProfileDto profileDto);

    void deleteProfileByUserId(Long userId);

    void recoverProfile(Long userId);

    void hardDeleteProfile(Long userId);

//
//
//    int calculateProfileCompletion(ProfileDto profileDto);
//
//    void changeVisibility(Long userId, String visibility);
}