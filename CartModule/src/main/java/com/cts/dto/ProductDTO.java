package com.cts.dto;

import lombok.Data;

@Data
public class ProductDTO {
	private Integer bookId;
    private String bookName;
    private double bookprice;
    private int quantity;

}
