package com.salonbooking.salonservice.mapper;

import com.salonbooking.salonservice.modal.Salon;
import com.salonbooking.salonservice.payload.dto.SalonDTO;

public class SalonMapper {
    public static SalonDTO mapSalonToDTO(Salon salon) {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(salon.getId());
        salonDTO.setName(salon.getName());
        salonDTO.setEmail(salon.getEmail());
        salonDTO.setCity(salon.getCity());
        salonDTO.setImages(salon.getImages());
        salonDTO.setCloseTime(salon.getCloseTime());
        salonDTO.setOpenTime(salon.getOpenTime());
        salonDTO.setPhoneNumber(salon.getPhoneNumber());
        salonDTO.setAddress(salon.getAddress());
        salonDTO.setOwnerId(salon.getOwnerId());
        return salonDTO;
    }
}
