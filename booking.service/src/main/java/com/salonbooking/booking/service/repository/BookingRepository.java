package com.salonbooking.booking.service.repository;

import com.salonbooking.booking.service.modal.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findBySalonId(Long salonId);
}
