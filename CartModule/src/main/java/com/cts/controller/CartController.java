package com.cts.controller;

import java.util.List;

//import org.modelmapper.ModelMapper;
import com.cts.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cts.dto.CartDTO;
import com.cts.dto.CartItemDTO;
import com.cts.service.ICartService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

	@Autowired
	private ICartService cartService;



	@PostMapping("/{userId}/addproduct/{bookId}")
	public ResponseEntity<CartDTO> addToCart(@PathVariable Integer userId, @PathVariable Long bookId,@Valid @RequestBody ProductDTO productdto) {
		return ResponseEntity.ok(cartService.addProductToCart(userId, productdto,bookId));
	}

	@PutMapping("/{userId}/increaseqnty/{bookId}")
	public ResponseEntity<CartDTO> increaseProductQuantity(@PathVariable Integer userId,
			@PathVariable Long bookId, @Valid @RequestParam Integer quantityToAdd) {

		CartDTO updatedCart = cartService.increaseProductQuantity(userId, bookId, quantityToAdd);
		return ResponseEntity.ok(updatedCart);
	}

	@PutMapping("/{userId}/decreaseqnty/{bookId}")
	public ResponseEntity<CartDTO> decreaseProductQuantity(@PathVariable Integer userId,
			@PathVariable Long bookId, @Valid @RequestParam Integer quantityToRemove) {

		CartDTO updatedCart = cartService.decreaseProductQuantity(userId, bookId, quantityToRemove);
		return ResponseEntity.ok(updatedCart);
	}

	@DeleteMapping("/{userId}/removeBook/{bookId}")
	public ResponseEntity<String> removeFromCart(@PathVariable Integer userId, @PathVariable Long bookId) {
		cartService.removeProductFromCart(userId, bookId);
		return ResponseEntity.ok("Book removed successfully.");
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
