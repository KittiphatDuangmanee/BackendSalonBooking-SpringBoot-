package com.salonbooking.payment.service.messaging;

import com.salonbooking.payment.service.modal.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventProducer {

    private final RabbitTemplate rabbitTemplate;
    public void sentBookingUpdateEvent(PaymentOrder paymentOrder) {
        rabbitTemplate.convertAndSend("booking-queue", paymentOrder);
    }
}
