package com.cts.entity;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long catId;

	@NotBlank
	private String catName;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Book> books;
	
	private LocalDateTime catCreatedDate;
	private boolean isCatDeleted;
	
}