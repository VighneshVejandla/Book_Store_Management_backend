package com.cts.dto;

//import com.cts.entity.Inventory;

import lombok.Data;

@Data
public class BookDTO {
	
	private Long author_id;
	private Long book_id;
	private Long category_id;
	private int price;
	private int stock_quantity;
	private String title;
//	private Inventory inventory;
}
