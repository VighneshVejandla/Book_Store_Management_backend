package com.cts.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catId;

    @NotBlank
    private String catName;

    public Long getCatId() {
		return catId;
	}
	public void setCatId(Long catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Book> books;

    private LocalDateTime catCreatedDate;
    private boolean isCatDeleted;
}
