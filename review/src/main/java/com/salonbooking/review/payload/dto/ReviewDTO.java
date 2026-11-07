package com.salonbooking.review.payload.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private UserDTO user;
    private Long salonId;
    private String reviewText;
    private double rating;
    private LocalDateTime createdAt;
}
