package com.salonbooking.offering.service.controller;

import com.salonbooking.offering.service.dto.CategoryDTO;
import com.salonbooking.offering.service.dto.SalonDTO;
import com.salonbooking.offering.service.dto.ServiceOfferingDTO;
import com.salonbooking.offering.service.modal.ServiceOffering;
import com.salonbooking.offering.service.service.ServiceOfferingService;
import com.salonbooking.offering.service.service.client.CategoryFeignClient;
import com.salonbooking.offering.service.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.RequestPath;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-offering/salon-owner")
@RequiredArgsConstructor
public class SalonServicesOfferingController {
    private final ServiceOfferingService serviceOfferingService;
    private final SalonFeignClient salonFeignClient;
    private final CategoryFeignClient categoryFeignClient;

    @PostMapping
    public ResponseEntity<ServiceOffering> createServiceOffering(
            @RequestBody ServiceOfferingDTO serviceOfferingDTO,
            @RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salonDTO = salonFeignClient
                .getSalonByOwnerId(jwt).getBody();

        CategoryDTO categoryDTO = categoryFeignClient
                .getAllCategoriesByIdAndSalonId(serviceOfferingDTO.getCategoryId(), salonDTO.getId()).getBody();

        ServiceOffering newServiceOffering = serviceOfferingService.createServiceOffering(serviceOfferingDTO,
                salonDTO,
                categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newServiceOffering);
    }

    @PutMapping("/{serviceOfferingId}")
    public ResponseEntity<ServiceOffering> updateServiceOffering(@RequestBody ServiceOffering serviceOffering,
                                                                 @PathVariable Long serviceOfferingId) throws Exception {

        ServiceOffering updatedServiceOffering = serviceOfferingService.updateServiceOffering(serviceOfferingId,serviceOffering);
        return ResponseEntity.ok(updatedServiceOffering);
    }


}
