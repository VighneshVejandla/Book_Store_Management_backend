package com.cts.orderservice.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ResOrderDTO {
	
	private Long orderId;
	private Long userId;
	private LocalDate orderDate;
	private Double totalAmount;
	private String status;
	//private Map<Long, Integer> bookIdsWithQuantity;
	private List<ResBookDto> books;
}
