package com.cts.userservice.controller;

import com.cts.userservice.dto.OrderDTO;
import com.cts.userservice.dto.OrderSummaryDto;
import com.cts.userservice.service.ProfileServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.userservice.dto.ProfileDto;
import com.cts.userservice.service.IProfileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private IProfileService profileService;

    @Autowired
    ProfileServiceImplement profileServiceImplement;

    @PostMapping(value = "/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDto> createProfile(@RequestPart(value = "profileDto") ProfileDto profileDto,
                                                    @PathVariable Long userId,
                                                    @RequestPart(value ="image") MultipartFile image) {
        return new ResponseEntity<ProfileDto>(profileService.createProfile(profileDto, userId, image), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDto> updateProfile(@PathVariable Long userId, @RequestPart(value = "profileDto") ProfileDto profileDto,
                                                    @RequestPart(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<ProfileDto>(profileService.updateProfile(userId, profileDto, image), HttpStatus.OK);
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long userId) {
        return new ResponseEntity<ProfileDto>(profileService.getProfileByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long userId) {
        profileService.deleteProfileByUserId(userId);
        return new ResponseEntity<String >("Profile deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/harddelete/{userId}")
    public ResponseEntity<String> hardDeleteProfile(@PathVariable Long userId) {
        profileService.hardDeleteProfile(userId);
        return new ResponseEntity<String >("Profile deleted From the DataBase.", HttpStatus.OK);
    }

    @PutMapping("/recoverprofile/{userId}")
    public ResponseEntity<String> recoverProfile(@PathVariable Long userId) {
        profileService.recoverProfile(userId);
        return new ResponseEntity<String >("Profile Recovered successfully.", HttpStatus.OK);
    }

//---------------------------------------------------------------------------------

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId){
        List<OrderDTO> orders = profileServiceImplement.getUserOrders(userId);
        return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId){
        OrderDTO order = profileServiceImplement.getOrderById(orderId);
        return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
    }

    @DeleteMapping("/order/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId){
        OrderDTO cancelOrder = profileServiceImplement.cancelOrder(orderId);
        return new ResponseEntity<>(cancelOrder, HttpStatus.OK);
    }

    @GetMapping("/{userId}/orderSummary")
    public ResponseEntity<OrderSummaryDto> getUserOrderSummary(@PathVariable Long userId){
        OrderSummaryDto summary = profileServiceImplement.getUserOrderSummary(userId);
        return new ResponseEntity<OrderSummaryDto>( summary, HttpStatus.OK);
    }



    //    -----------------pushing the image into Cloudinary---------------
//    @PostMapping(value = "/uploadImage/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadProfileImage(@PathVariable Long userId, @RequestParam("image")MultipartFile file){
//        try{
//            ProfileDto profileDto = profileServiceImplement.uploadProfileImage(userId, file);
//            return new ResponseEntity<>(profileDto, HttpStatus.OK);
//        }catch (IOException e){
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INSUFFICIENT_SPACE_ON_RESOURCE)
//                    .body("Upload Failed : " + e.getMessage());
//        }
//
//    }

}

