package com.salonbooking.review.controller;

import com.salonbooking.review.mapper.ReviewMapper;
import com.salonbooking.review.modal.Review;
import com.salonbooking.review.payload.dto.*;
import com.salonbooking.review.service.client.SalonFeignClient;
import com.salonbooking.review.service.client.UserFeignClient;
import com.salonbooking.review.service.imp.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewServiceImpl reviewService;
    private final UserFeignClient userFeignClient;
    private final SalonFeignClient salonFeignClient;

    @PostMapping("/salon/{salonId}")
    public ResponseEntity<Review> createReview(
            @RequestBody ReviewRequest reviewRequest,
            @PathVariable Long salonId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        SalonDTO salonDTO = salonFeignClient.getSalonById(salonId).getBody();
        Review review = reviewService.createReview(reviewRequest, userDTO, salonDTO);
        return  ResponseEntity.ok(review);
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsBySalonId(@PathVariable Long salonId) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonById(salonId).getBody();
        List<Review> reviews = reviewService.getReviewsBySalonId(salonDTO.getId());
        List<ReviewDTO> reviewDTOS = reviews.stream().map((review) -> {
            UserDTO userDTO = null;
            try{
                userDTO = userFeignClient.getUserById(review.getUserId()).getBody();
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
            return ReviewMapper.toReviewDTO(review, userDTO);
        }).toList();
        return ResponseEntity.ok(reviewDTOS);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestBody ReviewRequest reviewRequest,
                                               @PathVariable Long reviewId,
                                               @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        Review review = reviewService.updateReview(reviewRequest, reviewId, userDTO.getId());
        return  ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,
                                               @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        reviewService.deleteReview(reviewId, userDTO.getId());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Deleted review successfully");
        return ResponseEntity.ok(apiResponse);

    }


}
