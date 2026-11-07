package com.salonbooking.booking.service.mapper;

import com.salonbooking.booking.service.dto.BookingDTO;
import com.salonbooking.booking.service.dto.SalonDTO;
import com.salonbooking.booking.service.dto.ServiceOfferingDTO;
import com.salonbooking.booking.service.dto.UserDTO;
import com.salonbooking.booking.service.modal.Booking;


import java.util.Set;

public class BookingMapper {
    public static BookingDTO toBookingDTO(Booking booking, Set<ServiceOfferingDTO> serviceOfferings, UserDTO userDTO, SalonDTO salonDTO) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setServiceIds(booking.getServiceIds());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        bookingDTO.setServiceOfferings(serviceOfferings);
        bookingDTO.setUserDTO(userDTO);
        bookingDTO.setSalonDTO(salonDTO);
        return bookingDTO;
    }
}
