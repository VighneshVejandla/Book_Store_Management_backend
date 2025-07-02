package com.cts.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.cts.dto.BookDTO;
import com.cts.dto.ResReviewDTO;
import com.cts.dto.UserDTO;
import com.cts.exception.BookNotFoundException;
import com.cts.exception.ReviewExistsException;
import com.cts.exception.UserNotFoundException;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.cts.dto.ReviewDTO;
import com.cts.entity.Review;
import com.cts.exception.ResourceNotFoundException;
import com.cts.feign.UserClient;
import com.cts.feign.BookClient;
import com.cts.repository.ReviewRepository;

@Service
public class ReviewServiceImplement implements IReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserClient userClient;

    @Autowired
    BookClient bookClient;
    
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ReviewDTO addReview(Long userId, Long bookId, ReviewDTO reviewDTO) {
        String username;

        try {
            UserDTO userDTO = userClient.viewUserById(userId).getBody();
            username = userDTO.getName();
        } catch (FeignException.NotFound fe) {
            throw new UserNotFoundException("User should be registered in order to be able to review");
        }

        try {
            BookDTO bookDTO = bookClient.viewBookById(bookId).getBody();
        } catch (FeignException.InternalServerError fe) {
            throw new BookNotFoundException("Book ID " + bookId + " does not exist in inventory");
        }

//        Optional<Review> checkReview = reviewRepository.findByUserIdAndBookId(userId, bookId);
//        if (checkReview.isPresent())
//            throw new ReviewExistsException("User " + username + " already reviewed this book");

        Review newReview = modelMapper.map(reviewDTO, Review.class);
        //ReviewDTO reviewDTO = new ReviewDTO();

        newReview.setCreatedAt(LocalDateTime.now());
        newReview.setUpdatedAt(LocalDateTime.now());
        newReview.setReviewDeleted(false);
        newReview.setRating(reviewDTO.getRating());
        newReview.setBookId(bookId);
        newReview.setUserId(userId);
        newReview.setCreatedAt(LocalDateTime.now());
        newReview.setUpdatedAt(LocalDateTime.now());
        newReview.setDownvotes(0);
        newReview.setUpvotes(0);
        newReview.setFlags(0);

        Review saveReview = reviewRepository.save(newReview);
        return modelMapper.map(saveReview, ReviewDTO.class);
    }

    @Override
    public List<ReviewDTO> viewAllReviews(Long userId) {
    	List<ReviewDTO> reviews = reviewRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("Review not found for User ID: " + userId);
        }
        
        return reviews;
    }

    @Override
    public List<ReviewDTO> getAllReviewsForBook(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


//    @Override
//    public ReviewDTO getReviewById(Long bookId) {
//        return reviewRepository.findByBookId(bookId)
//                .stream()
//                .findFirst()
//                .map(this::convertToDTO)
//                .orElseThrow(() -> new ResourceNotFoundException("Review not found for Book ID: " + bookId));
//    }

    @Override
    public ReviewDTO editReviewById(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        
        // Update editable fields.
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setUpdatedAt(LocalDateTime.now());
        
        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    @Override
    public ReviewDTO deleteReviewbyId(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        
        // Soft delete the review.
        review.setReviewDeleted(true);
        review.setUpdatedAt(LocalDateTime.now());
        
        Review deletedReview = reviewRepository.save(review);
        return convertToDTO(deletedReview);
    }

    // New method: Increment upvotes.
    @Override
    public ReviewDTO upvoteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        review.setUpvotes(review.getUpvotes() + 1);
        review.setUpdatedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    // New method: Increment down votes.
    @Override
    public ReviewDTO downvoteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        review.setDownvotes(review.getDownvotes() + 1);
        review.setUpdatedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    // New method: Increment flags.
    @Override
    public ReviewDTO flagReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Increment the flag counter.
        review.setFlags(review.getFlags() + 1);
        review.setUpdatedAt(LocalDateTime.now());

        // If the review has more than 10 flags, perform a soft delete.
        if (review.getFlags() > 10) {
            review.setReviewDeleted(true);
            review.setUpdatedAt(LocalDateTime.now());
            Review softDeletedReview = reviewRepository.save(review);
            return convertToDTO(softDeletedReview);
        }

        // Otherwise, simply update the review.
        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    
    @Override
    public void hardDeleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        reviewRepository.delete(review);
    }

    
    // Helper method to convert entity to DTO.
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setUserId(review.getUserId());
//        dto.setBookId(review.getBookId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
//        dto.setUpdatedAt(review.getUpdatedAt());
//        dto.setReviewDeleted(review.isReviewDeleted());
        dto.setUpvotes(review.getUpvotes());
        dto.setDownvotes(review.getDownvotes());
        dto.setFlags(review.getFlags());
        return dto;
    }

    @Override
    public List<ResReviewDTO> TrendingBooks(Long count) {
        return reviewRepository.findAll().stream()
                .collect(Collectors.groupingBy(r -> r.getBookId()))
                .values().stream()
                .map(reviews -> {
                    ResReviewDTO dto = modelMapper.map(reviews.get(0), ResReviewDTO.class);
                    double avg = reviews.stream().mapToDouble(Review::getRating).average().orElse(0);
                    dto.setRating(avg);                return dto;
                })
                .sorted(Comparator.comparingDouble(ResReviewDTO::getRating).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByMinRating(double minRating) {
//        List<Review> relevantReviews = reviewRepository.findByRatingGreaterThanEqual(minRating).stream()
//                .filter(review -> !review.isReviewDeleted())
//                .collect(Collectors.toList());
        List<Review> relevantReviews = reviewRepository.findAll().stream()
                .filter(review -> !review.isReviewDeleted() && review.getRating() >= minRating)
                .collect(Collectors.toList());


        if (relevantReviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found with a rating of " + minRating + " or higher.");
        }

        // 2. Extract unique book IDs from these reviews to avoid fetching duplicate book details
        Set<Long> uniqueBookIds = relevantReviews.stream()
                .map(Review::getBookId)
                .collect(Collectors.toSet()); // Use Set to ensure uniqueness

        // 3. Fetch book details for each unique book ID using the BookClient Feign client
        List<BookDTO> books = uniqueBookIds.stream()
                .map(bookId -> {
                    try {
                        ResponseEntity<BookDTO> response = bookClient.viewBookById(bookId);
                        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                            return response.getBody();
                        }
                        System.err.println("Warning: Book with ID " + bookId + " not found or unavailable from book service.");
                        return null; // Return null to filter out later
                    } catch (FeignException fe) {
                        System.err.println("Error fetching book with ID " + bookId + " from book service: " + fe.getMessage());
                        return null; // Return null to filter out later
                    } catch (Exception e) {
                        System.err.println("Unexpected error fetching book with ID " + bookId + ": " + e.getMessage());
                        return null; // Return null to filter out later
                    }
                })
                .filter(bookDTO -> bookDTO != null) // Filter out any books that couldn't be fetched
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            // This can happen if reviews were found, but none of the corresponding books could be fetched
            throw new ResourceNotFoundException("No books could be retrieved for the given rating criteria after checking the book service.");
        }

        return books;
    }
}
