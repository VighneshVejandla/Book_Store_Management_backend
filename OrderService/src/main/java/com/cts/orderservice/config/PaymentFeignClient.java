package com.cts.orderservice.config;

import com.cts.orderservice.dto.PaymentInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="PAYMENTSERVICE")
public interface PaymentFeignClient {
    @GetMapping("/payments/paymentdetails/{paymentId}")
    public PaymentInfoDTO viewPaymentDetails(@PathVariable Long paymentId);
}
