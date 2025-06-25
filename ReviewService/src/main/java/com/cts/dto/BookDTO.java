package com.cts.dto;

//import com.cts.entity.Inventory;

import lombok.Data;

@Data
public class BookDTO {

	private Long bookId;
	private int price;
	private int stockQuantity;
	private String title;
//	private Inventory inventory;
}
