package com.salonbooking.payment.service.controller;

import com.razorpay.RazorpayException;
import com.salonbooking.payment.service.domain.PaymentMethod;
import com.salonbooking.payment.service.dto.BookingDTO;
import com.salonbooking.payment.service.dto.UserDTO;
import com.salonbooking.payment.service.modal.PaymentOrder;
import com.salonbooking.payment.service.payload.response.PaymentLinkResponse;
import com.salonbooking.payment.service.service.client.UserFeignClient;
import com.salonbooking.payment.service.service.imp.PaymentServiceImpl;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    private final UserFeignClient  userFeignClient;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO bookingDTO,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();

        PaymentLinkResponse response = paymentService.createPaymentOrder(userDTO,bookingDTO,paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
            @PathVariable Long paymentOrderId
    ) throws Exception {
        PaymentOrder paymentOrderResponse = paymentService.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(paymentOrderResponse);
    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> processdPayment(
//        @RequestParam String paymentId,
        @RequestParam String paymentLinkId
    ) throws RazorpayException, StripeException {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentOrderId(paymentLinkId);
        Boolean response = paymentService.proceedPaymentOrder(paymentOrder,paymentLinkId);
        return ResponseEntity.ok(response);
    }



}
