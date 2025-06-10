package com.cts.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findByUserId(Long userId);
}
