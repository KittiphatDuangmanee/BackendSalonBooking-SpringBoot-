package com.salonbooking.notification.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String description;

    private Boolean isRead=false;

    private Long userId;

    private Long bookingId;

    private Long salonId;

    private LocalDateTime createdAt;
}
