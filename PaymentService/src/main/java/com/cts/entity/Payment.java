package com.cts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Payment {

	@Id
	private Long paymentId;
	//private Long orderId;
	private Long userId;
	private String upiId;
	private double amount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String status;

}
