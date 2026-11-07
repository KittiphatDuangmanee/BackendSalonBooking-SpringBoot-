package com.salonbooking.booking.service.service.client;

import com.salonbooking.booking.service.domain.PaymentMethod;
import com.salonbooking.booking.service.dto.BookingDTO;
import com.salonbooking.booking.service.dto.PaymentLinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {

    @PostMapping("/api/payments/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO bookingDTO,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt
    );
}
