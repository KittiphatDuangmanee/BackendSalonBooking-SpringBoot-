package com.salonbooking.review.service.imp;

import com.salonbooking.review.modal.Review;
import com.salonbooking.review.payload.dto.ReviewRequest;
import com.salonbooking.review.payload.dto.SalonDTO;
import com.salonbooking.review.payload.dto.UserDTO;
import com.salonbooking.review.repository.ReviewRepository;
import com.salonbooking.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    @Override
    public Review createReview(ReviewRequest reviewRequest, UserDTO userDTO, SalonDTO salonDTO) {
        Review review = new Review();
        review.setReviewText(reviewRequest.getReviewText());
        review.setRating(reviewRequest.getRating());
        review.setUserId(userDTO.getId());
        review.setSalonId(salonDTO.getId());
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsBySalonId(Long salonId) {
        return reviewRepository.findBySalonId(salonId);
    }

    private Review getReviewByReviewId(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new Exception("Reviews not found or not exist")
        );
    }

    @Override
    public Review updateReview(ReviewRequest reviewRequest, Long reviewId, Long userId) throws Exception {
        Review review = getReviewByReviewId(reviewId);
        if(!review.getUserId().equals(userId)){
            throw new Exception("You don't have permission to update review");
        }
        review.setReviewText(reviewRequest.getReviewText());
        review.setRating(reviewRequest.getRating());
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewByReviewId(reviewId);
        if(!review.getUserId().equals(userId)){
            throw new Exception("You don't have permission to update review");
        }
        reviewRepository.delete(review);
    }
}
