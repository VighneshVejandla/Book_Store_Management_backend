package com.cts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.validation.annotation.Validated;

import com.cts.dto.CartDTO;
import com.cts.dto.CartItemDTO;
import com.cts.entity.Cart;
import com.cts.entity.CartItem;
import com.cts.exception.CartNotFoundException;
import com.cts.exception.ProductNotFoundException;
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
	   	   
	    
	    @Transactional
	    public CartDTO addProductToCart(Integer userId, CartItemDTO cartItemDto) {
	        Cart cart = cartRepository.findCartByUserId(userId).orElseThrow(()->new UserNotFoundException("User not found with Id"+userId));
	                ;

	        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndBookId(cart, cartItemDto.getBookId());

	        if (existingItemOpt.isPresent()) {
	            CartItem existingItem = cartItemRepository.findById(existingItemOpt.get().getId())
	                    .orElseThrow(() -> new RuntimeException("CartItem not found!")); // Ensure fresh entity
	            existingItem.setQuantity(existingItem.getQuantity() + cartItemDto.getQuantity());
	            cartItemRepository.save(existingItem);
	        } else {
	            CartItem cartItem = modelMapper.map(cartItemDto, CartItem.class);
	            cartItem.setCart(cart);
	            cartItemRepository.save(cartItem);
	        }
	        cartRepository.save(cart);
	        return modelMapper.map(cart, CartDTO.class);
	    }
    	@Override  
	    @Transactional
	    public CartDTO increaseProductQuantity(Integer userId, Integer bookId, Integer quantityToAdd) {
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
	    public CartDTO decreaseProductQuantity(Integer userId, Integer bookId, Integer quantityToRemove) {
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
	}

	


