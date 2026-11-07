package com.salonbooking.payment.service.service.client;

import com.salonbooking.payment.service.dto.ServiceOfferingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("OFFERING-SERVICE")
public interface ServiceOfferingFeignClient {

    @GetMapping("/api/service-offering/list/{serviceIds}")
    public ResponseEntity<Set<ServiceOfferingDTO>> getServicesByServiceIds(
            @PathVariable Set<Long> serviceIds);
}
