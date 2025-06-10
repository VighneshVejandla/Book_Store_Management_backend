package com.cts.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.userservice.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUserUserId(Long userId);

}