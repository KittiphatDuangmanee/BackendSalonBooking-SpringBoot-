package com.salonbooking.booking.service.service.client;


import com.salonbooking.booking.service.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryFeignClient {
//    @GetMapping("/api/categories/{categoryId}")
//    public ResponseEntity<CategoryDTO> getCategoryById(
//            @PathVariable Long categoryId) throws Exception;

    @GetMapping("/api/categories/salon-owner/salon/{salonId}/category/{categoryId}")
    public ResponseEntity<CategoryDTO> getAllCategoriesByIdAndSalonId(
            @PathVariable Long categoryId,
            @PathVariable Long salonId) throws Exception;

}
