package com.salonbooking.review.mapper;

import com.salonbooking.review.modal.Review;
import com.salonbooking.review.payload.dto.ReviewDTO;
import com.salonbooking.review.payload.dto.UserDTO;

public class ReviewMapper {
    public static ReviewDTO toReviewDTO(Review review, UserDTO userDTO) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setUser(userDTO);
        reviewDTO.setRating(review.getRating());
        reviewDTO.setSalonId(review.getSalonId());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        return reviewDTO;
    }
}
