package com.cts.orderservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cts.orderservice.config.PaymentFeignClient;
import com.cts.orderservice.dto.*;
import com.cts.orderservice.exception.PaymentStatusException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.orderservice.config.BookFeignClient;
import com.cts.orderservice.config.UserFeignClient;
import com.cts.orderservice.entity.Order;
import com.cts.orderservice.exception.IdNotFoundException;
import com.cts.orderservice.exception.OutOfStockException;
import com.cts.orderservice.repository.OrderRepository;
import feign.FeignException;
import org.springframework.validation.annotation.Validated;

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

	@Autowired
	PaymentFeignClient paymentFeignClient;

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public ResOrderDTO addOrder(OrderDTO orderDTO) {

		logger.info("Received request to create order for userId: {}", orderDTO.getUserId());
		Map<Long,Integer> bookIds = orderDTO.getBookIdsWithQuantity();
		logger.debug("Fetching user from userid: {}",orderDTO.getUserId());
		logger.info("Check Payment Status");
		try {
			PaymentInfoDTO paymentDto = paymentFeignClient.viewPaymentDetails(orderDTO.getPaymentId());
			if(!(paymentDto.getStatus().equals("SUCCESS"))){
				throw new PaymentStatusException("Payment is not done !");
			}
		} catch (FeignException.BadRequest ex) {
			throw new PaymentStatusException("Invalid Payment ID: " + orderDTO.getPaymentId());
		}
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
		    if (book.getStockQuantity() < quantity) {
		        throw new OutOfStockException("Out of stock: Book ID " + book.getBookId());
		    }
		    book.setStockQuantity(book.getStockQuantity()-quantity);
		    //System.out.println(book);
			logger.debug("Updating the book stock");
		    BookDto bookdto = bookFeignClient.updatebook(bookId, book);
		    ResBookDto resbook = modelMapper.map(bookdto, ResBookDto.class);
		    resbook.setQuantity(quantity);
		    books.add(resbook);
		}
 
		// Convert DTO to entity and save the order
		Order order = ConvertDtoToEntity(orderDTO);
		order.setUserId(user.getUserId());
		order.setOrderCreatedDate(LocalDate.now());
		order.setOrderUpdatedDate(LocalDateTime.now());
		order.setOrderDeleted(false);
		logger.info("Saving the order");
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
		logger.info("Return the List of Books");
		return list;
	}
	
	@Override
	public ResOrderDTO getOrderByid(Long orderid) {
		Order order = orderRepository.findById(orderid).orElseThrow(()->new IdNotFoundException("Order id is not valid"));
		ResOrderDTO resorder = ConvertEntityToResDto(order);
		Map<Long,Integer> bookIds = order.getBookIdsWithQuantity();
		resorder.setBooks(ConvertBoodIdToBook(bookIds));
		logger.info("Return Order of id:{}",resorder.getOrderId());
		return resorder;
	}
	
	@Override
	public String deleteOrderById(Long orderid){
		if(!orderRepository.existsById(orderid)){
			throw new IdNotFoundException("Order ID is invalid");
		}
		orderRepository.deleteById(orderid);
		if(!orderRepository.existsById(orderid)) {
			logger.info("Order is cancelled");
			return "Order is Cancelled Successfully";
		}
		logger.info("Order is not cancelled Check for issue");
        return "Order is not cancelled";
    }
	
	public ResOrderDTO updateOrder(OrderDTO orderdto,Long orderid) {
		Order order = orderRepository.findById(orderid).orElseThrow(()->new IdNotFoundException("Order id is not valid"));
		order.setUserId(orderdto.getUserId());
		order.setTotalAmount(orderdto.getTotalAmount());
		order.setStatus(orderdto.getStatus());
		order.setBookIdsWithQuantity(orderdto.getBookIdsWithQuantity());
		order.setPaymentId(orderdto.getPaymentId());
		order.setOrderUpdatedDate(LocalDateTime.now());
		order.setOrderDeleted(false);
		Order savedOrder = orderRepository.save(order);
		ResOrderDTO order2 = ConvertEntityToResDto(savedOrder);
		Map<Long,Integer> bookIds = order.getBookIdsWithQuantity();
		order2.setBooks(ConvertBoodIdToBook(bookIds));
		logger.info("Updated the order");
		return order2;
	}

	public ResOrderDTO updateStatus(Long orderId, Map<String,String> status){
		Order order = orderRepository.findById(orderId).orElseThrow(()->new IdNotFoundException("Order id is not valid"));
		System.out.println(order);
		order.setStatus(status.get("status"));
		logger.info("Updated the order status");
		ResOrderDTO order2 = ConvertEntityToResDto(orderRepository.save(order));
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
