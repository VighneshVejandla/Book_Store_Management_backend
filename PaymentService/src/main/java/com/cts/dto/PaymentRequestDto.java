package com.cts.dto;

import lombok.Data;

@Data
public class PaymentRequestDto {
	// private Long paymentId;

	// @NotBlank(message = "UPI ID is mandatory.")
	private String upiId;

	// @NotBlank(message = "User ID is mandatory.")
	private Long userId;

	// @NotNull(message = "Amount is required for the transaction to proceed.")
	// @Min(value = 1, message = "Amount should be greater than or equal to 1.")
	private double amount;

	// @NotNull(message = "Order Id is required.")
	private Long orderId;

}
