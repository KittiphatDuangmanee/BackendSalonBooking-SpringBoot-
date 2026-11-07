package com.salonbooking.salonservice.service;

import com.salonbooking.salonservice.modal.Salon;
import com.salonbooking.salonservice.payload.dto.SalonDTO;
import com.salonbooking.salonservice.payload.dto.UserDTO;

import java.util.List;

public interface SalonService {
    Salon createSalon(SalonDTO salonDTO, UserDTO userDTO);

    Salon updateSalon(SalonDTO salonDTO, UserDTO userDTO, Long salonId) throws Exception;

    List<Salon> getAllSalons();

    Salon getSalonById(Long salonId) throws Exception;

    Salon getSalonByOwnerId(Long ownerId);

    List<Salon> serachSalonByCityName(String cityName);
}
