package com.cts.userservice.feignclient;

import com.cts.userservice.dto.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ReviewService")
public interface ReviewClient {
    @GetMapping("reviews/user/{userId}")
    List<ReviewDTO> getReviewsByUserId(@PathVariable Long userId);

    @DeleteMapping("/review/harddelete/{reviewId}")
    void hardDeleteReview(@PathVariable Long reviewId);

}
