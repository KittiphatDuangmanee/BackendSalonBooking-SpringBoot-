package com.salonbooking.category.service.controller;

import com.salonbooking.category.service.modal.Category;
import com.salonbooking.category.service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping("/salon/{salonId}")
  public ResponseEntity<Set<Category>> getAllCategoriesBySalonId(@PathVariable Long salonId){
      Set<Category> categories = categoryService.getAllCategoriesBySalonId(salonId);
      return ResponseEntity.ok(categories);
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) throws Exception{
      Category category = categoryService.getCategoryById(categoryId);
      return ResponseEntity.ok(category);
  }




}
