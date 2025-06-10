package com.cts.bookmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bookmanagement.dto.OrderDTO;
import com.cts.bookmanagement.dto.ResOrderDTO;
import com.cts.bookmanagement.service.IOrderService;

@RestController
@RequestMapping("/bookstore")
public class OrderController {
	
	@Autowired
	IOrderService orderService;
	
	@PostMapping("/addOrder")
	ResponseEntity<ResOrderDTO> addOrder(@RequestBody OrderDTO orderDTO) {
		return new ResponseEntity<ResOrderDTO>(orderService.addOrder(orderDTO), HttpStatus.OK);
	}
	@GetMapping("/getOrderByUserId/{id}")
	ResponseEntity<List<ResOrderDTO>> getOrderByUserId(@PathVariable Long id) {
		return new ResponseEntity<List<ResOrderDTO>>(orderService.getOrderByUserId(id), HttpStatus.OK);
	}
	@GetMapping("/getOrderById/{id}")
	ResponseEntity<ResOrderDTO> getOrderById(@PathVariable Long id) {
		return new ResponseEntity<ResOrderDTO>(orderService.getOrderByid(id), HttpStatus.OK);
	}
	@PutMapping("/updateOrderById/{id}")
	ResponseEntity<ResOrderDTO> updateOrderById(@RequestBody OrderDTO orderdto,@PathVariable Long id) {
		return new ResponseEntity<ResOrderDTO>(orderService.updateOrder(orderdto,id), HttpStatus.OK);
	}
	@DeleteMapping("/calcelOrder/{id}")
	ResponseEntity<String> cancelOrder(@PathVariable Long id){
		return new ResponseEntity<String>(orderService.cancelOrder(id),HttpStatus.OK);
	}
}
