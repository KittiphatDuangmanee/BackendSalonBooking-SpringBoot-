package com.salonbooking.review.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String reviewText;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private Long salonId;

    @Column(nullable = false)
    private Long userId;

    @CreatedDate
    private LocalDateTime createdAt;
}
