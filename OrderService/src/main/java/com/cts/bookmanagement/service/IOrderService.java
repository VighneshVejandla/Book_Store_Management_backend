package com.cts.bookmanagement.service;

import java.util.List;

import com.cts.bookmanagement.dto.OrderDTO;
import com.cts.bookmanagement.dto.ResOrderDTO;

public interface IOrderService {
	
	ResOrderDTO addOrder(OrderDTO orderDTO);
	List<ResOrderDTO> getOrderByUserId(Long userid);
	String cancelOrder(Long orderid);
	ResOrderDTO getOrderByid(Long orderid); 
	ResOrderDTO updateOrder(OrderDTO orderdto,Long orderid); 
}
