package com.salonbooking.booking.service.controller;

import com.salonbooking.booking.service.domain.BookingStatus;
import com.salonbooking.booking.service.domain.PaymentMethod;
import com.salonbooking.booking.service.dto.*;
import com.salonbooking.booking.service.mapper.BookingMapper;
import com.salonbooking.booking.service.modal.Booking;
import com.salonbooking.booking.service.modal.SalonReport;
import com.salonbooking.booking.service.service.client.PaymentFeignClient;
import com.salonbooking.booking.service.service.client.SalonFeignClient;
import com.salonbooking.booking.service.service.client.ServiceOfferingFeignClient;
import com.salonbooking.booking.service.service.client.UserFeignClient;
import com.salonbooking.booking.service.service.imp.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;
    private final SalonFeignClient  salonFeignClient;
    private final UserFeignClient userFeignClient;
    private final ServiceOfferingFeignClient serviceOfferingFeignClient;
    private final PaymentFeignClient  paymentFeignClient;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createBooking(
            @RequestParam Long salonId,
            @RequestParam PaymentMethod paymentMethod,
            @RequestBody  BookingRequest bookingRequest,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();

        SalonDTO salonDTO = salonFeignClient.getSalonById(salonId).getBody();

        Set<ServiceOfferingDTO> serviceOfferingDTOSet =  serviceOfferingFeignClient
                .getServicesByServiceIds(bookingRequest.getServiceIds()).getBody();
        
        if(serviceOfferingDTOSet.isEmpty()){
            throw new Exception("No services found");
        }

        Booking booking = bookingService.createBooking(bookingRequest,
                userDTO,
                salonDTO,
                serviceOfferingDTOSet);

        BookingDTO bookingDTO = BookingMapper.toBookingDTO(booking, serviceOfferingDTOSet, userDTO, salonDTO);
        PaymentLinkResponse response = paymentFeignClient.createPaymentLink(
                bookingDTO,
                paymentMethod,
                jwt
        ).getBody();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomerId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        if(userDTO == null || userDTO.getId() == null){
            throw new Exception("User not found from JWT");
        }
        List<Booking> bookings = bookingService.getBookingsByCustomerId(userDTO.getId());
        System.out.println("-------bookings data------: " + bookings);
        System.out.println("-------bookingdtos data------: " + getBookingDTOs(bookings));
        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream()
                .map(booking -> {
                    Set<ServiceOfferingDTO> serviceOfferingDTOSet = serviceOfferingFeignClient.getServicesByServiceIds(booking.getServiceIds()).getBody();
                    SalonDTO salon;
                    UserDTO user;
                    try {
                        user = userFeignClient.getUserById(booking.getCustomerId()).getBody();
                        salon = salonFeignClient.getSalonById(booking.getSalonId()).getBody();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return BookingMapper.toBookingDTO(booking,serviceOfferingDTOSet,user,salon);
                })
                .collect(Collectors.toSet());
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalonId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        List<Booking> bookings = bookingService.getBookingsBySalonId(salonDTO.getId());
        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(
            @PathVariable Long bookingId
    ) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);
        Set<ServiceOfferingDTO> serviceOfferingDTOSet = serviceOfferingFeignClient.getServicesByServiceIds(booking.getServiceIds()).getBody();
        UserDTO customer = userFeignClient.getUserById(booking.getCustomerId()).getBody();
        SalonDTO salon = salonFeignClient.getSalonById(booking.getSalonId()).getBody();
        return ResponseEntity.ok(BookingMapper.toBookingDTO(booking, serviceOfferingDTOSet, customer, salon));
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus bookingStatus
    ) throws Exception {
        Booking booking = bookingService.updateBookingStatus(bookingId,bookingStatus);
        Set<ServiceOfferingDTO> serviceOfferingDTOSet = serviceOfferingFeignClient.getServicesByServiceIds(booking.getServiceIds()).getBody();
        UserDTO customer = userFeignClient.getUserById(booking.getCustomerId()).getBody();
        SalonDTO salon = salonFeignClient.getSalonById(booking.getSalonId()).getBody();
        return ResponseEntity.ok(BookingMapper.toBookingDTO(booking, serviceOfferingDTOSet, customer, salon));
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookingsByDate(
            @PathVariable Long salonId,
            @PathVariable(required = false) LocalDate date
    ){
        List<Booking> bookings = bookingService.getBookingByDate(date,salonId);
        List<BookingSlotDTO> slotDTOS = bookings.stream()
                .map(booking -> {
                    BookingSlotDTO bookingSlotDTO = new BookingSlotDTO();
                    bookingSlotDTO.setStartTime(booking.getStartTime());
                    bookingSlotDTO.setEndTime(booking.getEndTime());
                    return bookingSlotDTO;
                }).toList();
        return ResponseEntity.ok(slotDTOS);

    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        SalonReport salonReport = bookingService.getSalonReport(salonDTO.getId());
        return ResponseEntity.ok(salonReport);
    }





}
