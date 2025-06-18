package com.cts.orderservice.dto;

import lombok.Data;

@Data
public class BookDto {
	private Long authId;
	private Long catId;
	private Long book_id;
	private int price;
	private int stock_quantity;
	private String title;
}
