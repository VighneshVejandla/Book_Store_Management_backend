package com.cts.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartDTO {
	private Integer cartId;
    private Integer userId;
    private List<CartItemDTO> cartItems;
    private double totalPrice;
}
