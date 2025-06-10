package com.cts.orderservice.dto;

import lombok.Data;

@Data
public class BookDto {
	private int author_id;
	private Long book_id;
	private int category_id;
	private Double price;
	private int stock_quantity;
	private String title;
}
