package com.salonbooking.review.repository;

import com.salonbooking.review.modal.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findBySalonId(Long salonId);

}
