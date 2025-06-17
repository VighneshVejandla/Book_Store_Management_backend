package com.cts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cts.Config.BookFeignClient;
import com.cts.Config.UserFeignClient;
import com.cts.dto.*;
import com.cts.exception.ProductNotFoundException;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cts.entity.Cart;
import com.cts.entity.CartItem;
import com.cts.exception.CartNotFoundException;
import com.cts.exception.UserNotFoundException;
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



		@Override
	    @Transactional
	    public CartDTO addProductToCart(Integer userId, ProductDTO productdto,Long bookId) {
			UserDto user = getUserById(userId);
			//System.out.println(user);
			BookSummaryDto bookData=getBookDetails(bookId).getBody();
			//BookSummaryDto bookData = bookFeignClient.getBookById(bookId).getBody();
			System.out.println(bookData);

	        Cart cart = cartRepository.findCartByUserId(userId).orElseThrow(()->new UserNotFoundException("User not found with Id"+userId));


	        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndBookId(cart, bookId);
			//System.out.println("The status of the optional item:"+existingItemOpt);
	        if (existingItemOpt.isPresent())
			{
				System.out.println("We are in if block");
	            CartItem existingItem = cartItemRepository.findById(existingItemOpt.get().getId())
	                    .orElseThrow(() -> new RuntimeException("CartItem not found!")); // Ensure fresh entity
	            existingItem.setQuantity(existingItem.getQuantity() + productdto.getQuantity());
	            cartItemRepository.save(existingItem);
	        } else
			{
				System.out.println("We are in the else block");
				CartItemDTO cartDetails=modelMapper.map(productdto,CartItemDTO.class);

				cartDetails.setBookId(bookData.getBookId());
				cartDetails.setBookName(bookData.getTitle());
				cartDetails.setBookPrice(bookData.getPrice());


				CartItem cartItem = new CartItem();
				cartItem.setBookId(bookData.getBookId());
				cartItem.setBookName(bookData.getTitle());
				cartItem.setBookPrice(bookData.getPrice());
				cartItem.setQuantity(productdto.getQuantity());
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
	        Cart cart = cartRepository.findCartByUserId(userId)
	                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

	        CartItem cartItem = cartItemRepository.findByCartAndBookId(cart, bookId)
	                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart!"));

	        //  Increase quantity
	        cartItem.setQuantity(cartItem.getQuantity() + quantityToAdd);
	        cartItemRepository.save(cartItem);

	        
	        
	        cartRepository.save(cart);

	        return modelMapper.map(cart, CartDTO.class);
	    }

    	@Override
	    @Transactional
	    public CartDTO decreaseProductQuantity(Integer userId, Long bookId, Integer quantityToRemove) {
	        Cart cart = cartRepository.findCartByUserId(userId)
	                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

	        CartItem cartItem = cartItemRepository.findByCartAndBookId(cart, bookId)
	                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart!"));

	        //  Decrease quantity, 
	        if (cartItem.getQuantity() > quantityToRemove) {
	            cartItem.setQuantity(cartItem.getQuantity() - quantityToRemove);
	        } else {
	            cartItemRepository.delete(cartItem); // If quantity becomes 0, remove item from cart
	        }

	      
	        return modelMapper.map(cart, CartDTO.class);
	    }

	    @Override
	    public void removeProductFromCart(Integer userId, Integer bookId) {
	        Cart cart = cartRepository.findCartByUserId(userId)
	                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
	        cart.getCartItems().removeIf(item -> item.getBookId().equals(bookId));
	        cartRepository.save(cart);
	    }

	    @Override
	    public void clearCart(Integer userId) {
	        Cart cart = cartRepository.findCartByUserId(userId)
	                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
	        cart.getCartItems().clear();
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
	                .map(item -> modelMapper.map(item, CartItemDTO.class))
	                .collect(Collectors.toList());
	    }
	public UserDto getUserById(Integer userId) {
		try {
			return userFeignClient.getUserId(userId);
		} catch (FeignException.NotFound e) {
			throw new UserNotFoundException("User ID " + userId + " not found.");
		}
	}

	public ResponseEntity<BookSummaryDto> getBookDetails(Long bookId){
			try
			{
				return bookFeignClient.getBookById(bookId);
			}
			catch(FeignException.NotFound e){
				throw new ProductNotFoundException("Book Details for BookId "+bookId+" not found");
			}
	}
	}

	


