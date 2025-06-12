package com.cts.Config;

import com.cts.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="USERSERVICE")
public interface UserFeignClient {

    @GetMapping("/user/viewuserbyid/{userId}")
    public UserDto getuser(@PathVariable Long userId);
}
