package com.cts.orderservice.dto;

import java.time.LocalDate;
import java.util.Map;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDTO {
	private Long orderId;

	@NotNull(message = "User ID cannot be null")
	private Long userId;

	@NotNull(message = "Total amount cannot be null")
	@DecimalMin(value = "0.0", message = "Total amount must be greater than 0.0")
	private Double totalAmount;

	@NotBlank(message = "Status cannot be blank")
	private String status;
	@NotNull(message="Payment id should not be null")
	private Long paymentId;
	private Map<Long, Integer> bookIdsWithQuantity;
}
