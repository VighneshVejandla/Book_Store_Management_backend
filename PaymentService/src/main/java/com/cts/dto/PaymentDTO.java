package com.cts.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long paymentId;
    //private Long orderId;
    private Long userId;
    private String upiId;
    private double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
}
