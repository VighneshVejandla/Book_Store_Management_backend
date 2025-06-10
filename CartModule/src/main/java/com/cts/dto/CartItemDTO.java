package com.cts.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemDTO {
	private Integer id;
	private Integer bookId;
    private String bookName;
    private double bookPrice;
    @Min(value = 0, message = "Quantity cannot be less than zero")
    private int quantity;

}
