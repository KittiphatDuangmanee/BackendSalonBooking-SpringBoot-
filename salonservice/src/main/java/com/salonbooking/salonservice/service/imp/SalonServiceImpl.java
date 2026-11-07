package com.salonbooking.salonservice.service.imp;

import com.salonbooking.salonservice.modal.Salon;
import com.salonbooking.salonservice.payload.dto.SalonDTO;
import com.salonbooking.salonservice.payload.dto.UserDTO;
import com.salonbooking.salonservice.repository.SalonRepository;
import com.salonbooking.salonservice.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDTO request, UserDTO userDTO) {
        Salon salon = new Salon();
        salon.setName(request.getName());
        salon.setAddress(request.getAddress());
        salon.setEmail(request.getEmail());
        salon.setCity(request.getCity());
        salon.setImages(request.getImages());
        salon.setOwnerId(userDTO.getId());
        salon.setPhoneNumber(request.getPhoneNumber());
        salon.setOpenTime(request.getOpenTime());
        salon.setCloseTime(request.getCloseTime());

        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(SalonDTO salonDTO, UserDTO userDTO, Long salonId) throws Exception {
        Salon existingSalon = salonRepository.findById(salonId).orElse(null);
        if(!salonDTO.getOwnerId().equals(userDTO.getId())){
            throw new Exception("You dont have permission to update the salon");
        }
        if(existingSalon != null) {
            existingSalon.setName(salonDTO.getName());
            existingSalon.setAddress(salonDTO.getAddress());
            existingSalon.setEmail(salonDTO.getEmail());
            existingSalon.setCity(salonDTO.getCity());
            existingSalon.setImages(salonDTO.getImages());
            existingSalon.setPhoneNumber(salonDTO.getPhoneNumber());
            existingSalon.setOpenTime(salonDTO.getOpenTime());
            existingSalon.setCloseTime(salonDTO.getCloseTime());
            existingSalon.setOwnerId(userDTO.getId());
            return salonRepository.save(existingSalon);
        }
        throw new Exception("Salon not exits");
    }

    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception {
        Salon salon = salonRepository.findById(salonId).orElse(null);
        if(salon == null){
            throw new Exception("Salon not exits");
        }
        return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> serachSalonByCityName(String cityName) {
        return salonRepository.searchSalons(cityName);
    }
}
