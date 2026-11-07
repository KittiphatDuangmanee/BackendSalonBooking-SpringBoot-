package com.salonbooking.offering.service.service.client;

import com.salonbooking.offering.service.dto.SalonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("SALON-SERVICE")
public interface SalonFeignClient {
    @GetMapping("/api/salons/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(
            @RequestHeader("Authorization") String jwt) throws Exception;
}
