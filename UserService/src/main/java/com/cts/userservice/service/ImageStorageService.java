package com.cts.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ImageStorageService {
    String saveImage(MultipartFile imageFile, Long userId) throws IOException;
}
