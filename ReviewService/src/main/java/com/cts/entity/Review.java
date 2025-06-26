package com.cts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	private Long userId;
	private Long bookId;
	
	@Min(value = 0, message = "Very Bad")
	@Max(value = 5, message = "Very Good")
	private double rating;
	
	private String comment;
	private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	private boolean isReviewDeleted;
	
	// Extra fields:
    private int upvotes = 0;
    private int downvotes = 0;
    private int flags = 0;
}
