package com.cts.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.dto.UserDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(value = "USERSERVICE", url = "http://localhost:8009")
public interface UserClient {
   
	@CircuitBreaker(name = "UserService", fallbackMethod = "getFallbackUser")
    @GetMapping("/user/viewuserbyid/{userId}")
    ResponseEntity<UserDTO> viewUserById(@PathVariable Long userId);

    default UserDTO getFallbackUser(Long id, Throwable ex) {
        return new UserDTO();
    }
}
