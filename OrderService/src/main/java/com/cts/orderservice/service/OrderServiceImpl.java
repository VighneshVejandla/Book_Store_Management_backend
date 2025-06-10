package com.cts.orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.orderservice.config.BookFeignClient;
import com.cts.orderservice.config.UserFeignClient;
import com.cts.orderservice.dto.BookDto;
import com.cts.orderservice.dto.OrderDTO;
import com.cts.orderservice.dto.ResBookDto;
import com.cts.orderservice.dto.ResOrderDTO;
import com.cts.orderservice.dto.UserDto;
import com.cts.orderservice.entity.Order;
import com.cts.orderservice.exception.IdNotFoundException;
import com.cts.orderservice.exception.OutOfStockException;
import com.cts.orderservice.repository.OrderRepository;

import feign.FeignException;


@Service
public class OrderServiceImpl implements IOrderService{
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	BookFeignClient bookFeignClient;
	
	@Autowired
	UserFeignClient userFeignClient;

	@Override
	public ResOrderDTO addOrder(OrderDTO orderDTO) {
		
		Map<Long,Integer> bookIds = orderDTO.getBookIdsWithQuantity();
		UserDto user = getUserById(orderDTO.getUserId());
		List<ResBookDto> books = new ArrayList<>();
		for (Map.Entry<Long, Integer> entry : bookIds.entrySet()) {
			
			Long bookId = entry.getKey();
			Integer quantity = entry.getValue();
			
		    BookDto book = bookFeignClient.getBooks(bookId);
		    //System.out.println(book);
		    if (book == null) {
		        throw new IdNotFoundException("Book ID " + bookId + " not found.");
		    }
		    if (book.getStock_quantity() < quantity) {
		        throw new OutOfStockException("Out of stock: Book ID " + book.getBook_id());
		    }
		    book.setStock_quantity(book.getStock_quantity()-quantity);
		    //System.out.println(book);
		    bookFeignClient.updatebook(bookId, book);
		    ResBookDto resbook = modelMapper.map(book, ResBookDto.class);
		    resbook.setQuantity(quantity);
		    books.add(resbook);
		}
 
		// Convert DTO to entity and save the order
		Order order = ConvertDtoToEntity(orderDTO);
		order.setUserId(user.getUserId());
		order.setOrderCreatedDate(LocalDateTime.now());
		order.setOrderDate(LocalDate.now());
		order.setOrderDeleted(false);

		Order savedOrder = orderRepository.save(order);
		ResOrderDTO order2 = ConvertEntityToResDto(savedOrder);
		order2.setBooks(books);
		return order2;
	}
	
	@Override
	public List<ResOrderDTO> getOrderByUserId(Long userid){
		
		List<Order> orders = orderRepository.findByUserId(userid);
		List<ResOrderDTO> list= ConvertListEntityToResDto(orders);
		for(ResOrderDTO dto : list) {
			Map<Long,Integer> bookIds = orders.get(list.indexOf(dto)).getBookIdsWithQuantity();
			dto.setBooks(ConvertBoodIdToBook(bookIds));
		}
		return list;
	}
	
	@Override
	public ResOrderDTO getOrderByid(Long orderid) {
		Order order = orderRepository.findById(orderid).orElseThrow(()->new IdNotFoundException("Order id is not valid"));
		ResOrderDTO resorder = ConvertEntityToResDto(order);
		Map<Long,Integer> bookIds = order.getBookIdsWithQuantity();
		resorder.setBooks(ConvertBoodIdToBook(bookIds));
		return resorder;
	}
	
	@Override
	public String cancelOrder(Long orderid){
		if(!orderRepository.existsById(orderid)){
			throw new IdNotFoundException("Order ID is invalid");
		}
		orderRepository.deleteById(orderid);
		if(!orderRepository.existsById(orderid)) {
			return "Order is Cancelled Successfully";
		}
        return "Order is not cancelled";
    }
	
	public ResOrderDTO updateOrder(OrderDTO orderdto,Long orderid) {
		Order order = orderRepository.findById(orderid).orElseThrow(()->new IdNotFoundException("Order id is not valid"));
		order.setUserId(orderdto.getUserId());
		order.setTotalAmount(orderdto.getTotalAmount());
		order.setStatus(orderdto.getStatus());
		order.setBookIdsWithQuantity(orderdto.getBookIdsWithQuantity());
		order.setOrderCreatedDate(LocalDateTime.now());
		order.setOrderDeleted(false);
		Order savedOrder = orderRepository.save(order);
		ResOrderDTO order2 = ConvertEntityToResDto(savedOrder);
		Map<Long,Integer> bookIds = order.getBookIdsWithQuantity();
		order2.setBooks(ConvertBoodIdToBook(bookIds));
		return order2;
	}
	
//------------------Conversion of Dto <->Entity methods----------------------------------------------
	
	public Order ConvertDtoToEntity(OrderDTO orderDTO) {
		return modelMapper.map(orderDTO, Order.class);
	}
	public ResOrderDTO ConvertEntityToResDto(Order order) {
		return modelMapper.map(order, ResOrderDTO.class);
	}
	public OrderDTO ConvertEntityToDto(Order order) {
		return modelMapper.map(order, OrderDTO.class);
	}
	public List<ResOrderDTO> ConvertListEntityToResDto(List<Order> orders){
		return orders.stream()
        .map(order -> modelMapper.map(order, ResOrderDTO.class)) // Convert each Order entity to DTO
        .collect(Collectors.toList());
	}
	
//-----------------------------------------------------------------------------------------------------------	
	
	public UserDto getUserById(Long userId) {
	    try {
	        return userFeignClient.getuser(userId);
	    } catch (FeignException.NotFound e) {
	        throw new IdNotFoundException("User ID " + userId + " not found.");
	    }
	}
	
	public List<ResBookDto> ConvertBoodIdToBook(Map<Long,Integer> bookIds) {
		List<ResBookDto> books = new ArrayList<>();
		for (Map.Entry<Long, Integer> entry : bookIds.entrySet()) {
			BookDto book = bookFeignClient.getBooks(entry.getKey());
			ResBookDto resbook = modelMapper.map(book, ResBookDto.class);
			resbook.setQuantity(entry.getValue());
			books.add(resbook);
		}
		return books;
	}

}
