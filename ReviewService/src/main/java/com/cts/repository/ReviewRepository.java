package com.cts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByBookId(Long bookId);
    List<Review> findByUserId(Long userId);

    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);
    List<Review> findByRatingGreaterThanEqual(double rating);
}
