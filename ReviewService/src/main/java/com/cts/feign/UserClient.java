package com.cts.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.dto.UserDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(value = "userservice", url = "http://localhost:8083")
public interface UserClient {
   
	@CircuitBreaker(name = "UserService", fallbackMethod = "getFallbackUser")
    @GetMapping("/user/{id}")
    UserDTO getUserById(@PathVariable Long id);

    default UserDTO getFallbackUser(Long id, Throwable ex) {
        return new UserDTO();
    }
}
