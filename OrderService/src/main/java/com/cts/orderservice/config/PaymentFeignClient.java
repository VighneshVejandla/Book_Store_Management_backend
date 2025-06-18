package com.cts.orderservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url="http://localhost:9997", value="e_commerce_payment_service")
public interface PaymentFeignClient {
    @GetMapping("/payments/viewstatus/{paymentId}")
    public String viewPaymentStatus(@PathVariable Long paymentId);
}
