package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class OrderStatusUpdateRequest {

	private Long paymentId;

	private String status;

}
