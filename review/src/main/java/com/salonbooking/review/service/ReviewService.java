package com.salonbooking.review.service;

import com.salonbooking.review.modal.Review;
import com.salonbooking.review.payload.dto.ReviewRequest;
import com.salonbooking.review.payload.dto.SalonDTO;
import com.salonbooking.review.payload.dto.UserDTO;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewRequest reviewRequest,
                        UserDTO userDTO,
                        SalonDTO salonDTO);
    List<Review> getReviewsBySalonId(Long salonId);

    Review updateReview(ReviewRequest reviewRequest, Long reviewId, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;


}
