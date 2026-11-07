package com.salonbooking.category.service.service;

import com.salonbooking.category.service.dto.SalonDTO;
import com.salonbooking.category.service.modal.Category;
import java.util.Set;

public interface CategoryService {
    Category createCategory(Category category , SalonDTO salonDTO);
    Set<Category> getAllCategoriesBySalonId(Long salonId);
    Category getCategoryById(Long categoryId) throws Exception;
    void deleteCategoryById(Long categoryId , Long salonId) throws Exception;
    Category findByIdAndSalonId(Long categoryId, Long salonId) throws Exception;
}
