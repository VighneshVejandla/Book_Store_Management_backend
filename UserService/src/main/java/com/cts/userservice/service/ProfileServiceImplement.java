package com.cts.userservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cts.userservice.dto.OrderDTO;
import com.cts.userservice.dto.OrderSummaryDto;
import com.cts.userservice.dto.ReviewDTO;
import com.cts.userservice.exception.ProfileExceptions;
import com.cts.userservice.exception.ResourceNotFoundException;
import com.cts.userservice.exception.UserNotFoundByIdException;
import com.cts.userservice.feignclient.OrderFeignClient;
import com.cts.userservice.feignclient.ReviewClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.userservice.dto.ProfileDto;
import com.cts.userservice.entity.Profile;
import com.cts.userservice.entity.User;
import com.cts.userservice.repository.ProfileRepository;
import com.cts.userservice.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileServiceImplement implements IProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ReviewClient reviewClient;

    @Autowired
    OrderFeignClient orderFeignClient;

    @Autowired
    ImageStorageService imageStorageService;

    private ProfileDto mapToDto(Profile profile) {
        return modelMapper.map(profile, ProfileDto.class);
    }

    private Profile mapToEntity(ProfileDto profileDto) {
        return modelMapper.map(profileDto, Profile.class);
    }

    @Override
    public ProfileDto createProfile(ProfileDto profileDto, Long userId, MultipartFile image) {

        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UserNotFoundByIdException("User", "id", userId));

        if (profileRepository.findByUserUserId(userId) != null) {
            throw new ProfileExceptions.ProfileAlreadyExistsException("Profile already exists for user ID: " + userId);
        }

        Profile profile = mapToEntity(profileDto);

        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        profile.setUser(user);

        if(image != null && !image.isEmpty()){
            try {
                String path = imageStorageService.saveImage(image, userId);
                profile.setProfileImageUrl(path);
            }catch (IOException e){
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        Profile savedProfile = profileRepository.save(profile);
        return mapToDto(savedProfile);
    }

    @Override
    public ProfileDto getProfileByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "UserId", userId));

        if(user.isDeleted()){
            throw new ResourceNotFoundException("User", "UserId", userId);
        }

        return Optional.ofNullable(profileRepository.findByUserUserId(userId))
                .map(profile -> {
                    if(profile.isDeleted()){
                        throw new ResourceNotFoundException("Profile", "UserId", userId + " not Found");
                    }
                    return mapToDto(profile);
                })
                .orElseThrow(() -> new ProfileExceptions.ProfileIsInActiveException("Profile"+userId+"is Already is In active"));
    }

    @Override
    public ProfileDto updateProfile(Long userId, ProfileDto profileDto, MultipartFile image) {

        return Optional.ofNullable(profileRepository.findByUserUserId(userId))
                .map(profile -> {
                    profile.setBio(profileDto.getBio());
                    profile.setPhoneNumber(profileDto.getPhoneNumber());
                    profile.setAddress(profileDto.getAddress());
                    profile.setUpdatedAt(LocalDateTime.now());
                    if(image != null && !image.isEmpty()){
                        try{
                            profile.setProfileImageUrl(imageStorageService.saveImage(image, userId));
                        }catch (IOException e){
                            throw new RuntimeException("Image Upload failed : " + e.getMessage());
                        }
                    }
                    return profileRepository.save(profile);
                }).map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "UserId", userId));

    }

    @Override
    public void deleteProfileByUserId(Long userId) {

        Optional.ofNullable((profileRepository.findByUserUserId(userId)))
                .ifPresentOrElse(
                        profile -> {
                            if(profile.isDeleted() == false) {
                                profile.setDeleted(true);
                                profileRepository.save(profile);
                            }else{
                                throw new ResourceNotFoundException("profile", "userId", userId );
                            }
                        },
                        ()->{
                            throw new ResourceNotFoundException("User", "userId", userId);
                        }
                );
    }

    @Override
    public void recoverProfile(Long userId) {

        User user = userRepository.findById(userId)
                .filter(u-> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("user", "UserId", userId));

        Profile profile = Optional.ofNullable(profileRepository.findByUserUserId(userId))
                .filter(Profile::isDeleted)
                .orElseThrow(() -> new ProfileExceptions.ProfileIsInActiveException("Profile with userId "+userId+" is in active"));

        profile.setDeleted(false);
        profileRepository.save(profile);
    }


    public void hardDeleteProfile(Long userId) {
        Profile profile = Optional.ofNullable(profileRepository.findByUserUserId(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "userId", userId));

        if (profile.getUser() == null || profile.getUser().getUserId() == null) {
            throw new ResourceNotFoundException("User", "userId", userId);
        }

        if (profile.isDeleted()) {
            User user = profile.getUser();
            user.setProfile(null);

            List<ReviewDTO> reviews = reviewClient.getReviewsByUserId(userId);
            reviews.forEach(review -> reviewClient.hardDeleteReview(review.getReviewId()));

            profileRepository.delete(profile);
        } else {
            throw new ResourceNotFoundException("Profile must be soft deleted before hard deleting", "userId", userId);
        }
    }

    public void deleteAllReviewsForUser(Long userId){
        List<ReviewDTO> reviews = reviewClient.getReviewsByUserId(userId);
        reviews.forEach(review -> reviewClient.hardDeleteReview(review.getReviewId()));

    }


    public List<OrderDTO> getUserOrders(Long userId){
        return orderFeignClient.getOrdersByUserId(userId);
    }

    public OrderDTO getOrderById(Long orderId){
        return orderFeignClient.getOrderById(orderId);
    }

    public OrderDTO cancelOrder(Long orderId){
        return orderFeignClient.cancelOrder(orderId);
    }

    public OrderSummaryDto getUserOrderSummary(Long userId){
        List<OrderDTO> orders = orderFeignClient.getOrdersByUserId(userId);
        int totalOrders = orders.size();
        double totalAmount = orders.stream()
                .mapToDouble(OrderDTO::getTotalAmount)
                .sum();

        return new OrderSummaryDto(totalOrders, totalAmount);
    }

    //    -------Service for Cloudinary-----------------

//    public ProfileDto uploadProfileImage(Long userId, MultipartFile file) throws IOException{
//        Profile profile = Optional.ofNullable(profileRepository.findByUserUserId(userId))
//                .orElseThrow(() -> new ResourceNotFoundException("Profile", "UserId", userId));
//
//        String imageUrl = cloudinaryService.uploadImage(file);
//        profile.setProfileImageUrl(imageUrl);
//        profile.setUpdatedAt(LocalDateTime.now());
//
//        return mapToDto(profileRepository.save(profile));
//
//    }


}