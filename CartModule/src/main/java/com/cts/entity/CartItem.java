package com.cts.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name="CartItem")
public class CartItem {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private Long bookId;
    private String bookName;
    private double bookPrice; 
    
    private int quantity;
    @Version
    private Integer version;




}
