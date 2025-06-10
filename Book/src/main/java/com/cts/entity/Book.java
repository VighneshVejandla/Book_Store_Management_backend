package com.cts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name ="book")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long book_id;
	
	public long getBook_id() {
		return book_id;
	}


	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}


	public int getAuthor_id() {
		return author_id;
	}


	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}


	public int getCategory_id() {
		return category_id;
	}


	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public int getStock_quantity() {
		return stock_quantity;
	}


	public void setStock_quantity(int stock_quantity) {
		this.stock_quantity = stock_quantity;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	private int author_id;
	private int category_id;
	
	@Min(value = 0, message = "This is a free book")
	private int price;
	
	
	@Min(value = 0, message = "Out of Stock!")
	private int stock_quantity;
	
	
	private String title;
	
//	@OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
//	@JsonBackReference
//
//	private Inventory inventory;
}