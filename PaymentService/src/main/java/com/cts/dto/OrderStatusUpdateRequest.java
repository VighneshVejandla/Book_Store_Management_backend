package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class OrderStatusUpdateRequest {

	private Long paymentId;

	private String status;

	private LocalDateTime updatedTime;

}
