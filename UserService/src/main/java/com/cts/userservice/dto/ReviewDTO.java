package com.cts.userservice.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    
    private Long reviewId;
    private Long userId;
    private Long bookId;
    private double rating;
    
    private String comment;
    
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private boolean isReviewDeleted;
//    
    private int upvotes;
    private int downvotes;
    private int flags;
}
