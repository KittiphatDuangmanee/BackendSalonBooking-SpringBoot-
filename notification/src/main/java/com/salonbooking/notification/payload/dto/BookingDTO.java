package com.salonbooking.notification.payload.dto;


import com.salonbooking.notification.domain.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {

    private Long id;

    private Long salonId; //salonObject

    private Long customerId; //customerObject

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> serviceIds; //serviceOfferingObject

    private BookingStatus status=BookingStatus.PENDING;

    private int totalPrice;
}
