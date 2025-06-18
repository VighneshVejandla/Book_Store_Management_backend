package com.cts.feign;

import com.cts.dto.CartToPaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:9926", value="CARTMODULE")
public interface CartServiceClient {
    @GetMapping("/api/v1/cart/{userId}/amountForPayment")
    ResponseEntity<CartToPaymentDTO> calculateTotalPrice(@PathVariable Integer userId);
}
