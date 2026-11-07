package com.salonbooking.booking.service.service;

import com.salonbooking.booking.service.domain.BookingStatus;
import com.salonbooking.booking.service.dto.BookingRequest;
import com.salonbooking.booking.service.dto.SalonDTO;
import com.salonbooking.booking.service.dto.ServiceOfferingDTO;
import com.salonbooking.booking.service.dto.UserDTO;
import com.salonbooking.booking.service.modal.Booking;
import com.salonbooking.booking.service.modal.PaymentOrder;
import com.salonbooking.booking.service.modal.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest booking,
                          UserDTO userDTO,
                          SalonDTO salonDTO,
                          Set<ServiceOfferingDTO> serviceOfferingDTOSet) throws Exception;

    List<Booking> getBookingsByCustomerId(Long customerId);

    List<Booking> getBookingsBySalonId(Long salonId);

    Booking getBookingById(Long bookingId) throws Exception;

    Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception;

    List<Booking> getBookingByDate(LocalDate date, Long salonId);

    SalonReport getSalonReport(Long salonId);

    Booking bookingSuccess(PaymentOrder paymentOrder) throws Exception;

}
