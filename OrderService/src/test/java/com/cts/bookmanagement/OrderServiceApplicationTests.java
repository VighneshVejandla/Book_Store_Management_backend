package com.cts.bookmanagement;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.cts.orderservice.config.BookFeignClient;
import com.cts.orderservice.config.UserFeignClient;
import com.cts.orderservice.dto.*;
import com.cts.orderservice.entity.Order;
import com.cts.orderservice.repository.OrderRepository;
import com.cts.orderservice.service.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceApplicationTests {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookFeignClient bookFeignClient;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private ModelMapper modelMapper;

    private OrderDTO orderDTO;
    private Order order;
    private UserDto userDto;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        Map<Long, Integer> bookIdsWithQuantity = new HashMap<>();
        bookIdsWithQuantity.put(100L, 2);
        orderDTO.setBookIdsWithQuantity(bookIdsWithQuantity);

        order = new Order();
        order.setUserId(1L);
        order.setBookIdsWithQuantity(bookIdsWithQuantity);
        order.setOrderCreatedDate(LocalDateTime.now());

        userDto = new UserDto();
        userDto.setUserId(1L);

        bookDto = new BookDto();
        bookDto.setBook_id(100L);
        bookDto.setStock_quantity(5);
    }

    @Test
    void testAddOrder_Success() {
        when(userFeignClient.getuser(1L)).thenReturn(userDto);
        when(bookFeignClient.getBooks(100L)).thenReturn(bookDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(modelMapper.map(any(Order.class), eq(ResOrderDTO.class))).thenReturn(new ResOrderDTO());
        when(modelMapper.map(any(BookDto.class), eq(ResBookDto.class))).thenReturn(new ResBookDto());
        when(modelMapper.map(any(OrderDTO.class), eq(Order.class))).thenReturn(new Order());
        

        ResOrderDTO result = orderService.addOrder(orderDTO);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }
    @Test
    void testCancelOrder_Success() {
        when(orderRepository.existsById(1L)).thenReturn(true,false);
        String result = orderService.cancelOrder(1L);

        assertEquals("Order is Cancelled Successfully", result);
    }
}
