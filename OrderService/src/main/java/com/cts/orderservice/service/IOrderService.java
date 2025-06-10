package com.cts.orderservice.service;

import java.util.List;

import com.cts.orderservice.dto.OrderDTO;
import com.cts.orderservice.dto.ResOrderDTO;

public interface IOrderService {
	
	ResOrderDTO addOrder(OrderDTO orderDTO);
	List<ResOrderDTO> getOrderByUserId(Long userid);
	String cancelOrder(Long orderid);
	ResOrderDTO getOrderByid(Long orderid); 
	ResOrderDTO updateOrder(OrderDTO orderdto,Long orderid); 
}
