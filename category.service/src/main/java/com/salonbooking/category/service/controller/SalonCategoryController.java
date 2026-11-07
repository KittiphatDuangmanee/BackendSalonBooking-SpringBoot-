package com.salonbooking.category.service.controller;

import com.salonbooking.category.service.dto.SalonDTO;
import com.salonbooking.category.service.modal.Category;
import com.salonbooking.category.service.service.CategoryService;
import com.salonbooking.category.service.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {
    private final CategoryService categoryService;
    private final SalonFeignClient salonFeignClient;

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category,
            @RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        Category newCategory = categoryService.createCategory(category, salonDTO);
        return ResponseEntity.ok(newCategory);
    }

    @GetMapping("/salon/{salonId}/category/{categoryId}")
    public ResponseEntity<Category> getAllCategoriesByIdAndSalonId(
            @PathVariable Long categoryId,
            @PathVariable Long salonId) throws Exception{
        Category category = categoryService.findByIdAndSalonId(categoryId,salonId);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long categoryId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        categoryService.deleteCategoryById(categoryId,salonDTO.getId());
        return ResponseEntity.ok("Delete category successfully");
    }

}
