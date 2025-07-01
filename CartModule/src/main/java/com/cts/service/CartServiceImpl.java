package com.cts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cts.Config.BookFeignClient;
import com.cts.Config.InventoryFeignClient;
import com.cts.Config.UserFeignClient;
import com.cts.dto.*;
import com.cts.exception.*;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cts.entity.Cart;
import com.cts.entity.CartItem;
import com.cts.repository.CartItemRepository;
import com.cts.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
@Validated
public class CartServiceImpl implements ICartService{
		
		@Autowired
	    private  CartRepository cartRepository;
		@Autowired
	    private  CartItemRepository cartItemRepository;
		@Autowired
	    private  ModelMapper modelMapper;
		@Autowired
		UserFeignClient userFeignClient;
		@Autowired
		BookFeignClient bookFeignClient;
		@Autowired
		InventoryFeignClient inventoryFeignClient;



	@Override
	@Transactional
	public CartDTO addProductToCart(Integer userId, ProductDTO productdto, Long bookId) {
		UserDto user = getUserById(userId);

		BookSummaryDto bookData = getBookDetails(bookId).getBody();
		if (bookData == null) {
			throw new RuntimeException("Book details not found for bookId: " + bookId);
		}

		Cart cart = cartRepository.findCartByUserId(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with Id: " + userId));

		int requiredQty = productdto.getQuantity();

		int stockData = inventoryFeignClient.getStockByBookId(bookId);
		if ((stockData == 0)) {
			throw new RuntimeException("Stock data not found for bookId: " + bookId);
		}

		Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndBookId(cart, bookId);
		if (existingItemOpt.isPresent()) {
			CartItem existingItem = cartItemRepository.findById(existingItemOpt.get().getId())
					.orElseThrow(() -> new RuntimeException("CartItem not found!"));

			int updatedQty = existingItem.getQuantity() + requiredQty;

			if (updatedQty > stockData) {
				throw new OutOfStockException("Cannot increase quantity beyond available stock: " + stockData);
			}

			existingItem.setQuantity(updatedQty);
			cartItemRepository.save(existingItem);
		} else {
			if (requiredQty > stockData) {
				throw new OutOfStockException("Requested quantity exceeds available stock for bookId: " + bookId);
			}

			CartItem cartItem = new CartItem();
			cartItem.setBookId(bookData.getBookId());
			cartItem.setBookName(bookData.getTitle());
			cartItem.setBookPrice(bookData.getPrice());
			cartItem.setQuantity(requiredQty);
			cartItem.setCart(cart);
			cartItemRepository.save(cartItem);
		}

		double totalPrice = cartItemRepository.findByCart(cart).stream()
				.mapToDouble(item -> item.getBookPrice() * item.getQuantity())
				.sum();

		cart.setTotalPrice(totalPrice);
		cartRepository.save(cart);

		return modelMapper.map(cart, CartDTO.class);
	}


	@Override
	@Transactional
	public CartDTO increaseProductQuantity(Integer userId, Long bookId, Integer quantityToAdd) {
		if (quantityToAdd == null || quantityToAdd <= 0) {
			throw new IllegalArgumentException("Quantity to add must be a positive number.");
		}

		// Fetch available stock from inventory service
		int stockData = inventoryFeignClient.getStockByBookId(bookId);

		// Fetch cart
		Cart cart = cartRepository.findCartByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

		// Fetch cart item
		CartItem cartItem = cartItemRepository.findByCartAndBookId(cart, bookId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found in cart!"));

		// Check if total desired quantity exceeds stock
		int newQuantity = cartItem.getQuantity() + quantityToAdd;
		if (newQuantity > stockData) {
			throw new OutOfStockException("Requested quantity exceeds available stock for bookId: " + bookId);
		}

		// Calculate additional cost before updating quantity
		double additionalCost = cartItem.getBookPrice() * quantityToAdd;

		// Update quantity
		cartItem.setQuantity(newQuantity);
		cartItemRepository.save(cartItem);

		// Update cart total price
		cart.setTotalPrice(cart.getTotalPrice() + additionalCost);
		cartRepository.save(cart);

		// Return updated cart DTO
		return modelMapper.map(cart, CartDTO.class);
	}





	@Override
	@Transactional
	public CartDTO decreaseProductQuantity(Integer userId, Long bookId, Integer quantityToRemove) {
		if (quantityToRemove == null || quantityToRemove <= 0) {
			throw new IllegalArgumentException("Quantity to remove must be a positive number.");
		}

		Cart cart = cartRepository.findCartByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

		CartItem cartItem = cartItemRepository.findByCartAndBookId(cart, bookId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found in cart!"));

		double itemPrice = cartItem.getBookPrice();
		int currentQuantity = cartItem.getQuantity();

		if (currentQuantity > quantityToRemove) {
			cartItem.setQuantity(currentQuantity - quantityToRemove);
			cartItemRepository.save(cartItem);

			// Update total price
			double reduction = itemPrice * quantityToRemove;
			cart.setTotalPrice(cart.getTotalPrice() - reduction);
		} else {
			// Remove item and update total price
			double reduction = itemPrice * currentQuantity;
			cart.setTotalPrice(cart.getTotalPrice() - reduction);
			cartItemRepository.delete(cartItem);
		}

		cartRepository.save(cart);

		return modelMapper.map(cart, CartDTO.class);
	}



	@Override
	@Transactional
	public void removeProductFromCart(Integer userId, Long bookId) {
		Cart cart = cartRepository.findCartByUserId(userId)
				.orElseThrow(() -> new ProductNotFoundException("Cart not found for user ID: " + userId));

		Optional<CartItem> itemToRemoveOpt = cart.getCartItems().stream()
				.filter(item -> item.getBookId().equals(bookId))
				.findFirst();

		if (itemToRemoveOpt.isEmpty()) {
			throw new ProductNotFoundException("Book with ID " + bookId + " not found in cart.");
		}

		CartItem itemToRemove = itemToRemoveOpt.get();

		// Update total price safely
		double itemTotalPrice = itemToRemove.getBookPrice() * itemToRemove.getQuantity();
		cart.setTotalPrice(Math.max(0, cart.getTotalPrice() - itemTotalPrice));

		// Remove item from cart and database
		cart.getCartItems().remove(itemToRemove);
		cartItemRepository.delete(itemToRemove); // Ensure it's removed from DB

		cartRepository.save(cart);
	}




	@Override
	    public void clearCart(Integer userId) {
	        Cart cart = cartRepository.findCartByUserId(userId)
	                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
	        cart.getCartItems().clear();
			cart.setTotalPrice(0.0);
			cart.setGrandTotalPrice(0.0);
	        cartRepository.save(cart);
	    }

	    @Override
	    public double calculateTotalPrice(Integer userId) {
	        Cart cart = cartRepository.findCartByUserId(userId)
	                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
	         double totalPrice = cart.getCartItems().stream()
	                .mapToDouble(item -> item.getBookPrice() * item.getQuantity())
	                .sum();
	         cart.setTotalPrice(totalPrice);
	         return cart.getTotalPrice();   
	    }
	    
	    @Override
	    public CartDTO createCartForUser(Integer userId) {
	        if (cartRepository.findCartByUserId(userId).isPresent()) {
	            throw new RuntimeException("Cart already exists for user: " + userId);
	        }	        
	        Cart cart = new Cart();
	        cart.setUserId(userId);
	        cartRepository.save(cart);
	        return modelMapper.map(cart, CartDTO.class);
	    }
	    public CartDTO convertToDto(Cart cart) {
	        return modelMapper.map(cart, CartDTO.class);
	    }

	@Override
	public List<CartItemDTO> getCartItems(Integer userId) {
		Cart cart = cartRepository.findCartByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

		return cart.getCartItems().stream()
				.map(item -> {
					CartItemDTO cartItemDTO = modelMapper.map(item, CartItemDTO.class);

					ResponseEntity<BookSummaryDto> bookDetailsResponse = getBookDetails(item.getBookId());

					if (bookDetailsResponse.getStatusCode().is2xxSuccessful() && bookDetailsResponse.getBody() != null) {
						BookSummaryDto bookSummary = bookDetailsResponse.getBody();
						cartItemDTO.setBookName(bookSummary.getTitle());
						cartItemDTO.setBookPrice(bookSummary.getPrice());
						cartItemDTO.setImageBase64(bookSummary.getImageBase64());
					} else {
						System.err.println("Could not fetch book details for bookId: " + item.getBookId());
					}
					return cartItemDTO;
				})
				.collect(Collectors.toList());
	}

	public UserDto getUserById(Integer userId) {
		try {
			return userFeignClient.getUserId(userId);
		} catch (FeignException.NotFound e) {
			throw new UserNotFoundException("User ID " + userId + " not found.");
		}
	}

	public ResponseEntity<BookSummaryDto> getBookDetails(Long bookId) {
		try {
			return bookFeignClient.getBookById(bookId);
		} catch (FeignException.NotFound e) {
			throw new ProductNotFoundException("Book Details for BookId " + bookId + " not found");
		} catch (Exception e) { // Catch other potential Feign exceptions
			throw new RuntimeException("Error fetching book details for BookId " + bookId + ": " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void updateCartTotal(Integer userId, double grandTotal) {
		Cart cart = cartRepository.findCartByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

		cart.setGrandTotalPrice(grandTotal);
		if(grandTotal!=cart.getGrandTotalPrice()) {
			throw new CartUpdateException("the cart grandtotal is not updated for userId: "+userId);// Set the grand total directly
		}
		cartRepository.save(cart); // Persist the updated cart
	}

}

	


