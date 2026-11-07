package com.salonbooking.category.service.service.imp;

import com.salonbooking.category.service.dto.SalonDTO;
import com.salonbooking.category.service.modal.Category;
import com.salonbooking.category.service.repository.CategoryRepository;
import com.salonbooking.category.service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category, SalonDTO salonDTO) {
        category.setSalonId(salonDTO.getId());
        return categoryRepository.save(category);
    }

    @Override
    public Set<Category> getAllCategoriesBySalonId(Long salonId) {
        return categoryRepository.findBySalonId(salonId);
    }

    @Override
    public Category getCategoryById(Long categoryId) throws Exception {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            throw new Exception("Category not exits with id " + categoryId);
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long categoryId, Long salonId) throws Exception {
        Category category = getCategoryById(categoryId);
        if (!category.getSalonId().equals(salonId)) {
            throw new Exception("You don't have permission to delete this category");
        }
        categoryRepository.deleteById(categoryId);

    }

    @Override
    public Category findByIdAndSalonId(Long categoryId, Long salonId) throws Exception {
        Category category = categoryRepository.findByIdAndSalonId(categoryId, salonId);
        if (category == null) {
            throw  new Exception("Category not found");
        }
        return category;
    }
}
