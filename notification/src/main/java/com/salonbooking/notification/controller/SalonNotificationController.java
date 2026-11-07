package com.salonbooking.notification.controller;

import com.salonbooking.notification.mapper.NotificationMapper;
import com.salonbooking.notification.modal.Notification;
import com.salonbooking.notification.payload.dto.BookingDTO;
import com.salonbooking.notification.payload.dto.NotificationDTO;
import com.salonbooking.notification.service.NotificationService;
import com.salonbooking.notification.service.client.BookingFeignClient;
import com.salonbooking.notification.service.imp.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/salon-owner")
public class SalonNotificationController {
    private final NotificationServiceImpl notificationService;
    private final BookingFeignClient bookingFeignClient;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationBySalonId(
            @PathVariable Long salonId
    ){
        List<Notification> notifications = notificationService.getAllNotificationBySalonId(salonId);
        List<NotificationDTO> notificationDTOS = notifications.stream()
                .map((notification -> {
                    BookingDTO bookingDTO = null;
                    try {
                        bookingDTO = bookingFeignClient.getBookingById(notification.getBookingId()).getBody();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return NotificationMapper.toDTO(notification, bookingDTO);
                })).toList();
        return ResponseEntity.ok(notificationDTOS);
    }
}
