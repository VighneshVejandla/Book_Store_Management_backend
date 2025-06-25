package com.cts.userservice.controller;

import com.cts.userservice.dto.OrderDTO;
import com.cts.userservice.dto.OrderSummaryDto;
import com.cts.userservice.service.ProfileServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.userservice.dto.ProfileDto;
import com.cts.userservice.service.IProfileService;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private IProfileService profileService;

    @Autowired
    ProfileServiceImplement profileServiceImplement;

    @PostMapping("/create/{userId}")
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto, @PathVariable Long userId) {
        return new ResponseEntity<ProfileDto>(profileService.createProfile(profileDto, userId), HttpStatus.OK);
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long userId) {
        return new ResponseEntity<ProfileDto>(profileService.getProfileByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ProfileDto> updateProfile(@PathVariable Long userId, @RequestBody ProfileDto profileDto) {
        return new ResponseEntity<ProfileDto>(profileService.updateProfile(userId, profileDto), HttpStatus.OK);
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
}

