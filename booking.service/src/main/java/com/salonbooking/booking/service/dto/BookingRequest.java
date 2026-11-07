package com.salonbooking.booking.service.dto;

import com.salonbooking.booking.service.domain.BookingStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingRequest {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> serviceIds;

}
