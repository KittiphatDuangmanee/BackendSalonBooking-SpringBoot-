package com.salonbooking.booking.service.controller;

import com.salonbooking.booking.service.dto.SalonDTO;
import com.salonbooking.booking.service.modal.Booking;
import com.salonbooking.booking.service.service.BookingService;
import com.salonbooking.booking.service.service.client.SalonFeignClient;
import com.salonbooking.booking.service.service.imp.BookingChartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings/chart")
public class ChartController {
    private final BookingService bookingService;
    private final SalonFeignClient salonService;
    private final BookingChartServiceImpl bookingChartService;

    @GetMapping("/earnings")
    public ResponseEntity<List<Map<String, Object>>> getEarningsChartData(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        SalonDTO salon = salonService.getSalonByOwnerId(jwt).getBody();
        List<Booking> bookings = bookingService.getBookingsBySalonId(salon.getId());

        List<Map<String, Object>> chartData = bookingChartService.generateEarningsChartData(bookings);

        return ResponseEntity.ok(chartData);

    }
    @GetMapping("/bookings")
    public ResponseEntity<List<Map<String, Object>>> getBookingsChartData(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        SalonDTO salon = salonService.getSalonByOwnerId(jwt).getBody();
        List<Booking> bookings = bookingService.getBookingsBySalonId(salon.getId());

        List<Map<String, Object>> chartData = bookingChartService.generateBookingCountChartData(bookings);

        return ResponseEntity.ok(chartData);

    }
}
