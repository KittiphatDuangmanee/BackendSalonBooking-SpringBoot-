package com.salonbooking.booking.service.service.imp;

import com.salonbooking.booking.service.domain.BookingStatus;
import com.salonbooking.booking.service.dto.BookingRequest;
import com.salonbooking.booking.service.dto.SalonDTO;
import com.salonbooking.booking.service.dto.ServiceOfferingDTO;
import com.salonbooking.booking.service.dto.UserDTO;
import com.salonbooking.booking.service.modal.Booking;
import com.salonbooking.booking.service.modal.PaymentOrder;
import com.salonbooking.booking.service.modal.SalonReport;
import com.salonbooking.booking.service.repository.BookingRepository;
import com.salonbooking.booking.service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    @Override
    public Booking createBooking(BookingRequest booking,
                                 UserDTO userDTO,
                                 SalonDTO salonDTO,
                                 Set<ServiceOfferingDTO> serviceOfferingDTOSet) throws Exception {
        int totalDuration = serviceOfferingDTOSet.stream().mapToInt(ServiceOfferingDTO::getDuration).sum();

        LocalDateTime bookingStartTime = booking.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

        Boolean isSlotAvailable = isTimeSlotAvailable(salonDTO,bookingStartTime,bookingEndTime);

        int totalPrice = serviceOfferingDTOSet.stream().mapToInt(ServiceOfferingDTO::getPrice).sum();

        Set<Long> idList = serviceOfferingDTOSet.stream().map(ServiceOfferingDTO::getId)
                .collect(Collectors.toSet());

        Booking newBooking = new Booking();
        newBooking.setCustomerId(userDTO.getId());
        newBooking.setSalonId(salonDTO.getId());
        newBooking.setServiceIds(idList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(newBooking);
    }

    public Boolean isTimeSlotAvailable(SalonDTO salonDTO,
                                       LocalDateTime bookingStartTime,
                                       LocalDateTime bookingEndTime) throws Exception {

        List<Booking> existingBookings = getBookingsBySalonId(salonDTO.getId());
        LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salonDTO.getCloseTime().atDate(bookingStartTime.toLocalDate());

        System.out.println("***********" + salonOpenTime.toString());
        System.out.println("***********" + salonCloseTime.toString());
        System.out.println("***********" + bookingStartTime.toString());
        System.out.println("***********" + bookingEndTime.toString());

        if(bookingStartTime.isBefore(salonOpenTime)
                || bookingEndTime.isAfter(salonCloseTime)) {
            throw new Exception("Booking time must be within salon's working hours");
        }

        for(Booking existingBooking : existingBookings) {
            LocalDateTime existingBookingStartTime = existingBooking.getStartTime();
            LocalDateTime existingBookingEndTime = existingBooking.getEndTime();

            if(bookingStartTime.isBefore(existingBookingEndTime) &&
            bookingEndTime.isAfter(existingBookingStartTime)) {
                throw new Exception("Slot not available, choose different time.");
            }

            if(bookingStartTime.isEqual(existingBookingStartTime) ||
            bookingEndTime.isEqual(existingBookingEndTime)){
                throw new Exception("Slot not available, choose different time.");
            }
        }

        return true;
    }

    @Override
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalonId(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long bookingId) throws Exception {

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if(booking == null){
            throw new Exception("Booking not found with id " + bookingId);
        }

        return booking;
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingByDate(LocalDate date, Long salonId) {
        List<Booking> allBookings = getBookingsBySalonId(salonId);
        if(date == null){
            return allBookings;
        }

        return allBookings.stream()
                .filter(booking -> isSameDate(booking.getStartTime(),date) ||
                        isSameDate(booking.getEndTime(),date)).toList();


    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> bookings = getBookingsBySalonId(salonId);
        int totalEarning = bookings.stream()
                .mapToInt(Booking::getTotalPrice).sum();
        Integer totalBooking = bookings.size();

        List<Booking> cancelledBookings = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CANCELLED).toList();
        Double totalRefund = cancelledBookings.stream()
                .mapToDouble(Booking::getTotalPrice).sum();

        SalonReport salonReport = new SalonReport();
        salonReport.setSalonId(salonId);
        salonReport.setTotalRefund(totalRefund);
        salonReport.setCancelledBooking(cancelledBookings.size());
        salonReport.setTotalBooking(totalBooking);
        salonReport.setTotalEarnings(totalEarning);

        return salonReport;
    }

    @Override
    public Booking bookingSuccess(PaymentOrder paymentOrder) throws Exception {
        Booking existingBooking = getBookingById(paymentOrder.getBookingId());
        existingBooking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(existingBooking);
    }
}
