package com.cts.userservice.feignclient;

import com.cts.userservice.dto.CartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "CartModule", path = "/api/v1/cart")
public interface CartFeignClient {

    @PostMapping("/{userId}/createcart")
    String createCart(@PathVariable Long userId);

    @GetMapping("/{userId}/viewAllProducts")
    List<CartItemDTO> getCartItems(@PathVariable Long userId);

    @DeleteMapping("/{userId}/clearcart")
    String clearCart(@PathVariable Long userId);
}
