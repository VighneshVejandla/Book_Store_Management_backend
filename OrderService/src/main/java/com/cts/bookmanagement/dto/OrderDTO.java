package com.cts.bookmanagement.dto;

import java.time.LocalDate;
import java.util.Map;

import lombok.Data;

@Data
public class OrderDTO {
	
	private Long orderId;
	private Long userId;
	private LocalDate orderDate;
	private Double totalAmount;
	private String status;
	private Map<Long, Integer> bookIdsWithQuantity;
}
