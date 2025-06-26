package com.cts.service;

import java.util.List;

import com.cts.dto.BookDTO;
import com.cts.dto.ResReviewDTO;
import com.cts.dto.ReviewDTO;

public interface IReviewService {

    //	ReviewDTO addReview(ReviewDTO reviewDTO);
    ReviewDTO addReview(Long userId, Long bookId, ReviewDTO reviewDTO);

    List<ReviewDTO> viewAllReviews(Long userId);

    //ReviewDTO getReviewById(Long bookId);

    List<ReviewDTO> getAllReviewsForBook(Long bookId);

    ReviewDTO editReviewById(Long reviewId, ReviewDTO reviewDTO);

    ReviewDTO deleteReviewbyId(Long reviewId);

    ReviewDTO upvoteReview(Long reviewId);

    ReviewDTO downvoteReview(Long reviewId);

    ReviewDTO flagReview(Long reviewId);

    void hardDeleteReview(Long reviewId);

    List<BookDTO> getBooksByMinRating(double minRating);
    List<ResReviewDTO> TrendingBooks(Long count);
}
