package com.cts.feign;
import com.cts.dto.ProfileToPaymentDTO;
import com.cts.dto.UserToPaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8006", value = "USERSERVICE")
public interface UserClient
{    @GetMapping("/user/viewuserbyid/{userId}")
    ResponseEntity<UserToPaymentDTO> viewUserById(@PathVariable Long userId);

    @GetMapping("/profile/view/{userId}")
    ResponseEntity<ProfileToPaymentDTO> getProfile(@PathVariable Long userId);}

