package com.cts.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class OrderDTO {
	
	private Long orderId;
	private Long userId;
	private LocalDate orderDate;
	private Double totalAmount;
	private String status;
	private Map<Long, Integer> bookIdsWithQuantity;
}
