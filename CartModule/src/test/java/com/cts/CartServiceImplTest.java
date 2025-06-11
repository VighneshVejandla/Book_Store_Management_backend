package com.cts;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Collections;

import com.cts.dto.CartDTO;
import com.cts.dto.CartItemDTO;
import com.cts.entity.Cart;
import com.cts.entity.CartItem;
import com.cts.repository.CartItemRepository;
import com.cts.repository.CartRepository;
import com.cts.service.CartServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.mockito.MockitoAnnotations;

public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBookToCart_NewCart_NewItem() {
        Integer userId = 1;
        CartItemDTO cartItemDto = new CartItemDTO();
        cartItemDto.setBookId(101);
        cartItemDto.setQuantity(2);
        cartItemDto.setBookPrice(100.0);

        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setCartItems(Collections.emptyList());

        CartItem cartItem = new CartItem();
        cartItem.setBookId(101);
        cartItem.setQuantity(2);
        cartItem.setBookPrice(100.0);
        cartItem.setCart(newCart);

        CartDTO cartDto = new CartDTO();

        when(cartRepository.findCartByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);
        when(cartItemRepository.findByCartAndBookId(any(), eq(101))).thenReturn(Optional.empty());
        when(modelMapper.map(cartItemDto, CartItem.class)).thenReturn(cartItem);
        when(modelMapper.map(any(Cart.class), eq(CartDTO.class))).thenReturn(cartDto);

        CartDTO result = cartService.addProductToCart(userId, cartItemDto);

        assertNotNull(result);
        verify(cartRepository, times(2)).save(any(Cart.class));
        verify(cartItemRepository).save(any(CartItem.class));
    }
}
