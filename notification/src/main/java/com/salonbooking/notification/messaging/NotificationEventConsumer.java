package com.salonbooking.notification.messaging;

import com.salonbooking.notification.modal.Notification;
import com.salonbooking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "notification-queue")
    public void sentNotificationEventConsumer(Notification notification) throws Exception {
        notificationService.createNotification(notification);
    }
}
