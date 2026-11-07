package com.salonbooking.notification.mapper;


import com.salonbooking.notification.modal.Notification;
import com.salonbooking.notification.payload.dto.BookingDTO;
import com.salonbooking.notification.payload.dto.NotificationDTO;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification,
                                        BookingDTO bookingDTO) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setBookingId(bookingDTO.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setBooking(bookingDTO);
        notificationDTO.setDescription(notification.getDescription());
        notificationDTO.setIsRead(notification.getIsRead());
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        notificationDTO.setUserId(notification.getUserId());
        notificationDTO.setSalonId(notification.getSalonId());

        return notificationDTO;
    }
}
