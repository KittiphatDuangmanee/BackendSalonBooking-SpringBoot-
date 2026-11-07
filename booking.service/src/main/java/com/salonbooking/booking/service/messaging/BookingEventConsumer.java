package com.salonbooking.booking.service.messaging;

import com.salonbooking.booking.service.modal.PaymentOrder;
import com.salonbooking.booking.service.service.imp.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final BookingServiceImpl bookingService;

    @RabbitListener(queues = "booking-queue")
    public void bookingUpdateListener(PaymentOrder paymentOrder) throws Exception {
        bookingService.bookingSuccess(paymentOrder);
    }
}
