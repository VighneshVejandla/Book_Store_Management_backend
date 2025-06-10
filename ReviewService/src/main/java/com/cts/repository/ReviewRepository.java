package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByBookId(Long bookId);
    List<Review> findByUserId(Long userId);
}
