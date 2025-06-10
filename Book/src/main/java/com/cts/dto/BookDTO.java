package com.cts.dto;

//import com.cts.entity.Inventory;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDTO {
	
	private int author_id;
	private int book_id;
	private int category_id;
	private int price;
	private int stock_quantity;
	private String title;


    public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

    public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

    public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

    public void setPrice(int price) {
		this.price = price;
	}

    public void setStock_quantity(int stock_quantity) {
		this.stock_quantity = stock_quantity;
	}

    public void setTitle(String title) {
		this.title = title;
	}
//	private Inventory inventory;
}
