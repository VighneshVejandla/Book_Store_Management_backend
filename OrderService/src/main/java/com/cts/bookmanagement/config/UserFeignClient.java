package com.cts.bookmanagement.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.bookmanagement.dto.UserDto;


@FeignClient(name="USERSERVICE")
public interface UserFeignClient {
	
	@GetMapping("/user/viewuserbyid/{userId}")
	public UserDto getuser(@PathVariable Long userId);
}
