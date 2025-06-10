package com.cts.bookmanagement.dto;

import lombok.Data;

@Data
public class ResBookDto {
	private Long book_id;
	private String title;
	private Double price;
	private int Quantity;
}
