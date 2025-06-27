package com.cts.orderservice.service;

import java.util.List;
import java.util.Map;

import com.cts.orderservice.dto.OrderDTO;
import com.cts.orderservice.dto.ResOrderDTO;

public interface IOrderService {
	
	ResOrderDTO addOrder(OrderDTO orderDTO);
	List<ResOrderDTO> getOrderByUserId(Long userid);
	String deleteOrderById(Long orderid);
	ResOrderDTO getOrderByid(Long orderid); 
	ResOrderDTO updateOrder(OrderDTO orderdto,Long orderid);
	ResOrderDTO updateStatus(Long orderId, Map<String,String> status);
	List<ResOrderDTO> getAllOrders();
}
