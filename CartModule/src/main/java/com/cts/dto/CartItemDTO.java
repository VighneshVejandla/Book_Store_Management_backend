package com.cts.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemDTO {

	private Long bookId;
    private String bookName;
    private Double bookPrice;
    @Min(value = 0, message = "Quantity cannot be less than zero")
    private int quantity;

}
