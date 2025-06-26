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
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long catId;

	@NotBlank(message = "Category name must not be blank")
	@Size(max=100, message="Category name must be not exceed 100 characters")
	private String catName;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Book> books;
	
	private LocalDateTime catCreatedDate;
	private boolean isCatDeleted;
	
}