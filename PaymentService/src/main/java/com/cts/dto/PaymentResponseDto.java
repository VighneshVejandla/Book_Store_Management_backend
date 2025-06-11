package com.cts.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentResponseDto {
	private Long paymentId;
	private String upiUri;
	private LocalDateTime createdAt;
	private String status;
	private String upiId;
	private Long userId;
	private Long orderId;
	private double amount;

	public PaymentResponseDto(Long paymentId, String upiUri, LocalDateTime createdAt, String status, String upiId,
			Long userId, Long orderId, double amount) {
		super();
		this.paymentId = paymentId;
		this.upiUri = upiUri;
		this.createdAt = createdAt;
		this.status = status;
		this.upiId = upiId;
		this.userId = userId;
		this.amount = amount;
		this.orderId = orderId;
	}

}
