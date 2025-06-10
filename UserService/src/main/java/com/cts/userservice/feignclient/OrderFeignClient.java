package com.cts.userservice.feignclient;


import com.cts.userservice.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "OrderService", url = "http://localhost:9082/bookstore")
public interface OrderFeignClient {

    @GetMapping("/getOrderByUserId/{userId}")
    List<OrderDTO> getOrdersByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/getOrderById/{orderId}")
    OrderDTO getOrderById(@PathVariable("orderId") Long orderId);

    @DeleteMapping("/calcel/{orderId}")
    OrderDTO cancelOrder(@PathVariable("orderId") Long orderId);

}
