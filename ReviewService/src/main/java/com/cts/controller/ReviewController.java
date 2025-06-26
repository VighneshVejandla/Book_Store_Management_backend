package com.cts.controller;

import java.util.List;

import com.cts.dto.BookDTO;
import com.cts.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.ReviewDTO;
import com.cts.service.IReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	@Autowired
    private IReviewService reviewService;

//    @PostMapping("/addreview")
//    public ResponseEntity<ReviewDTO> addReview(@RequestBody ReviewDTO reviewDTO) {
//        return ResponseEntity.ok(reviewService.addReview(reviewDTO));
//    }

    @PostMapping("/addreview/{userId}/{bookId}")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.addReview(userId, bookId, reviewDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> viewReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.viewAllReviews(userId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ReviewDTO> getReviewByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewById(bookId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> editReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.editReviewById(reviewId, reviewDTO));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReviewbyId(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
    
    // New endpoint: Upvote a review.
    @PostMapping("/{reviewId}/upvote")
    public ResponseEntity<ReviewDTO> upvoteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.upvoteReview(reviewId));
    }

    // New endpoint: Downvote a review.
    @PostMapping("/{reviewId}/downvote")
    public ResponseEntity<ReviewDTO> downvoteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.downvoteReview(reviewId));
    }

    // New endpoint: Flag a review.
    @PostMapping("/{reviewId}/flag")
    public ResponseEntity<ReviewDTO> flagReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.flagReview(reviewId));
    }
    
    // New endpoint: Hard delete a review (permanent deletion).
    @DeleteMapping("/hard/{reviewId}")
    public ResponseEntity<String> hardDeleteReview(@PathVariable Long reviewId) {
        reviewService.hardDeleteReview(reviewId);
        return ResponseEntity.ok("Review permanently deleted successfully");
    }

    @GetMapping("/trendingBooks/{count}")
    public ResponseEntity<List<ReviewDTO>> trendingBooks(@PathVariable Long count){
        return new ResponseEntity<List<ReviewDTO>>(reviewService.TrendingBooks(count), HttpStatus.OK);
    }

    @GetMapping("/books/by-min-rating")
    public ResponseEntity<?> getBooksByMinRating(@RequestParam double minRating) {
        try {
            List<BookDTO> books = reviewService.getBooksByMinRating(minRating);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error occurred while fetching books: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
