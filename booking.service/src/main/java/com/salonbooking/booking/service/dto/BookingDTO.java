package com.salonbooking.booking.service.dto;

import com.salonbooking.booking.service.domain.BookingStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class BookingDTO {

    private Long id;

    private Long salonId; //salonObject

    private Long customerId; //customerObject

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> serviceIds; //serviceOfferingObject

    private BookingStatus status = BookingStatus.PENDING;

    private Set<ServiceOfferingDTO> serviceOfferings;

    private UserDTO userDTO;

    private SalonDTO salonDTO;

    private int totalPrice;
}
