package com.cts.userservice.service;

import com.cts.userservice.exception.UserNotFoundByIdException;
import com.cts.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserImageServiceImpl implements IUserImageService {

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    UserRepository userRepository;

    @Override
    public String uploadUserImage(Long userId, MultipartFile imageFile) throws IOException {
        userRepository.findById(userId)
                .filter(u -> !u.isDeleted()) // As per your existing user validation logic
                .orElseThrow(() -> new UserNotFoundByIdException("User", "id", userId));
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty image file.");
        }
        String imagePath = imageStorageService.saveImage(imageFile, userId);
        return imagePath;
    }
}