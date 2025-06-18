package com.cts.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentInfoDTO {
    private Long paymentId;
    private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;
    private String status;
    private String upiId;
    private Long userId;
    private double amount;
}
