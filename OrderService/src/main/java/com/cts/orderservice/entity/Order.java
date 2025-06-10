package com.cts.orderservice.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orderId;
	
	private long userId;
	private LocalDate orderDate;
	private double totalAmount;
	private String status;
	private LocalDateTime orderCreatedDate;
	private boolean isOrderDeleted;
	
	@ElementCollection
    @CollectionTable(name = "order_books", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "book_id") // Maps book ID as key
    @Column(name = "quantity") // Stores quantity as value
    private Map<Long, Integer> bookIdsWithQuantity;

}
