package com.salonbooking.offering.service.controller;

import com.salonbooking.offering.service.modal.ServiceOffering;
import com.salonbooking.offering.service.service.imp.ServiceOfferingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/service-offering")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingServiceImpl serviceOfferingService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(
            @PathVariable Long salonId,
            @RequestParam(required = false) Long categoryId){

        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServiceOfferings(salonId, categoryId);

        return ResponseEntity.ok(serviceOfferings);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> getServicesByServiceId(@PathVariable Long serviceId) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingService.getServiceOfferingById(serviceId);
        return ResponseEntity.ok(serviceOffering);
    }

    @GetMapping("/list/{serviceIds}")
    public ResponseEntity<Set<ServiceOffering>> getServicesByServiceIds(@PathVariable Set<Long> serviceIds) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getServiceOfferings(serviceIds);
        return ResponseEntity.ok(serviceOfferings);
    }

}
