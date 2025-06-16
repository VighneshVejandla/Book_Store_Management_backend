package com.cts.service;

import java.util.List;

import com.cts.dto.CartDTO;
import com.cts.dto.CartItemDTO;
import com.cts.dto.ProductDTO;

public interface ICartService {
	CartDTO addProductToCart(Integer userId, ProductDTO productdto,Integer bookId);
	CartDTO increaseProductQuantity(Integer userId, Integer bookId, Integer quantityToAdd);
	CartDTO decreaseProductQuantity(Integer userId, Integer bookId, Integer quantityToRemove);
    void removeProductFromCart(Integer userId, Integer bookId);
    void clearCart(Integer userId);
    double calculateTotalPrice(Integer userId);
    CartDTO createCartForUser(Integer userId);

    
    List<CartItemDTO> getCartItems(Integer userId);

    
}
