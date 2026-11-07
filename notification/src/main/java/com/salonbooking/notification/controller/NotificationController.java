package com.salonbooking.notification.controller;

import com.salonbooking.notification.mapper.NotificationMapper;
import com.salonbooking.notification.modal.Notification;
import com.salonbooking.notification.payload.dto.BookingDTO;
import com.salonbooking.notification.payload.dto.NotificationDTO;
import com.salonbooking.notification.service.NotificationService;
import com.salonbooking.notification.service.client.BookingFeignClient;
import com.salonbooking.notification.service.imp.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationServiceImpl notificationService;
    private final BookingFeignClient bookingFeignClient;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @RequestBody Notification  notification
    ) throws Exception {
       return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationByUserId(
            @PathVariable Long userId
    ){
        List<Notification> notifications = notificationService.getAllNotificationByUserId(userId);
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

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(
            @PathVariable Long notificationId
    ) throws Exception {
        Notification notification = notificationService.markNotificationAsRead(notificationId);
        BookingDTO bookingDTO = bookingFeignClient.getBookingById(notification.getBookingId()).getBody();
        return ResponseEntity.ok(NotificationMapper.toDTO(notification, bookingDTO));
    }
}
