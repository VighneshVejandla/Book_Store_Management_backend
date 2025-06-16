package com.cts.controller;

import java.util.List;

//import org.modelmapper.ModelMapper;
import com.cts.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cts.dto.CartDTO;
import com.cts.dto.CartItemDTO;
import com.cts.service.ICartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

	@Autowired
	private ICartService cartService;



	@PostMapping("/{userId}/addproduct/{bookId}")
	public ResponseEntity<CartDTO> addToCart(@PathVariable Integer userId, @PathVariable Integer bookId,@Valid @RequestBody ProductDTO productdto) {
		return ResponseEntity.ok(cartService.addProductToCart(userId, productdto,bookId));
	}

	@PutMapping("/{userId}/increaseqnty/{productId}")
	public ResponseEntity<CartDTO> increaseProductQuantity(@PathVariable Integer userId,
			@PathVariable Integer productId, @Valid @RequestParam Integer quantityToAdd) {

		CartDTO updatedCart = cartService.increaseProductQuantity(userId, productId, quantityToAdd);
		return ResponseEntity.ok(updatedCart);
	}

	@PutMapping("/{userId}/decreaseqnty/{productId}")
	public ResponseEntity<CartDTO> decreaseProductQuantity(@PathVariable Integer userId,
			@PathVariable Integer productId, @Valid @RequestParam Integer quantityToRemove) {

		CartDTO updatedCart = cartService.decreaseProductQuantity(userId, productId, quantityToRemove);
		return ResponseEntity.ok(updatedCart);
	}

	@DeleteMapping("/{userId}/removeproduct/{productId}")
	public ResponseEntity<String> removeFromCart(@PathVariable Integer userId, @PathVariable Integer productId) {
		cartService.removeProductFromCart(userId, productId);
		return ResponseEntity.ok("Product removed successfully.");
	}

	@DeleteMapping("/{userId}/clearcart")
	public ResponseEntity<String> clearCart(@PathVariable Integer userId) {
		cartService.clearCart(userId);
		return ResponseEntity.ok("Cart cleared successfully.");
	}

	@PostMapping("/{userId}/createcart")
	public ResponseEntity<String> createCart(@PathVariable int userId) {
		try {
			cartService.createCartForUser(userId);
			return ResponseEntity.ok("Cart created successfully"); // Explicit success response
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating cart: " + e.getMessage()); // More useful error handling
		}
	}

	@GetMapping("/{userId}/total-price")
	public ResponseEntity<Double> getTotalPrice(@PathVariable Integer userId) {
		double totalPrice = cartService.calculateTotalPrice(userId);
		return ResponseEntity.ok(totalPrice);
	}

	@GetMapping("/{userId}/viewAllProducts")
	public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable Integer userId) {
		return ResponseEntity.ok(cartService.getCartItems(userId));
	}


}
