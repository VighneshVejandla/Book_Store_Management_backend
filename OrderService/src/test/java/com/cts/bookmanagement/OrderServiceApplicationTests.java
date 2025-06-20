package com.cts.bookmanagement;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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
    private ResOrderDTO resOrderDTO;
    private ResBookDto resBookDto;
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
        orderDTO.setTotalAmount(200.0); // New total amount
        orderDTO.setStatus("SHIPPED");

        order = new Order();
        order.setOrderId(1L);
        order.setUserId(1L);
        order.setBookIdsWithQuantity(bookIdsWithQuantity);
        order.setOrderCreatedDate(LocalDate.now());

        resOrderDTO = new ResOrderDTO();
        resOrderDTO.setOrderId(order.getOrderId()); // Set the order ID
        resOrderDTO.setUserId(order.getUserId());   // Set the user Id
        resOrderDTO.setTotalAmount(200.0); //

        userDto = new UserDto();
        userDto.setUserId(1L);

        bookDto = new BookDto();
        bookDto.setBookId(100L);
        bookDto.setPrice(125.0);
        bookDto.setStockQuantity(5);

        resBookDto = new ResBookDto();
        resBookDto.setBookId(100L);
        resBookDto.setQuantity(2);
    }

    @Test
    void testAddOrder_Success() {
        when(userFeignClient.getuser(1L)).thenReturn(userDto);
        when(bookFeignClient.getBooks(100L)).thenReturn(bookDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(modelMapper.map(any(Order.class), eq(ResOrderDTO.class))).thenReturn(new ResOrderDTO());
        when(modelMapper.map(any(BookDto.class), eq(ResBookDto.class))).thenReturn(new ResBookDto());
        when(bookFeignClient.updatebook(eq(100L), any(BookDto.class))).thenReturn(bookDto);
        when(modelMapper.map(any(OrderDTO.class), eq(Order.class))).thenReturn(new Order());

        ResOrderDTO result = orderService.addOrder(orderDTO);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }
    @Test
    void testCancelOrder_Success() {
        when(orderRepository.existsById(1L)).thenReturn(true,false);
        String result = orderService.deleteOrderById(1L);

        assertEquals("Order is Cancelled Successfully", result);
    }

    @Test
    void testorderbyid(){

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(any(Order.class), eq(ResOrderDTO.class))).thenReturn(resOrderDTO);
        when(bookFeignClient.getBooks(100L)).thenReturn(bookDto);
        when(modelMapper.map(bookDto, ResBookDto.class)).thenReturn(resBookDto);
        ResOrderDTO result = orderService.getOrderByid(1L);

        // Verify results
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(1L, result.getUserId());
        assertEquals(200.0, result.getTotalAmount());

    }

    @Test
    void testupdateorder(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        when(modelMapper.map(any(Order.class), eq(ResOrderDTO.class))).thenReturn(resOrderDTO);

        when(bookFeignClient.getBooks(100L)).thenReturn(bookDto);
        when(modelMapper.map(bookDto, ResBookDto.class)).thenReturn(resBookDto);

        ResOrderDTO result = orderService.updateOrder(orderDTO,1L);
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(1L, result.getUserId());
        assertEquals(200.0, result.getTotalAmount());

    }
}
