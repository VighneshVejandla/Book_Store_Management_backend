package com.cts.orderservice.dto;

import lombok.Data;

@Data
public class ResBookDto {
	private Long bookId;
	private String title;
	private Double price;
	private int Quantity;
}
