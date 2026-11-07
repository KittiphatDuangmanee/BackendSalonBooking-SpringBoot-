package com.salonbooking.payment.service.repository;

import com.salonbooking.payment.service.modal.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findByPaymentLinkId(String paymentLinkId);
}
