package com.salonbooking.salonservice.controller;

import com.salonbooking.salonservice.mapper.SalonMapper;
import com.salonbooking.salonservice.modal.Salon;
import com.salonbooking.salonservice.payload.dto.SalonDTO;
import com.salonbooking.salonservice.payload.dto.UserDTO;
import com.salonbooking.salonservice.service.SalonService;
import com.salonbooking.salonservice.service.client.UserFeignClient;
import com.salonbooking.salonservice.service.imp.SalonServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonServiceImpl salonService;
    private final UserFeignClient userFeignClient;

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(
            @RequestBody SalonDTO salonDTO,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        if(userDTO == null){
            throw new Exception("User not found from JWT");
        }
        Salon salon = salonService.createSalon(salonDTO,userDTO);
        SalonDTO responseSalonDTO = SalonMapper.mapSalonToDTO(salon);
        return  new ResponseEntity<>(responseSalonDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(
            @PathVariable Long salonId,
            @RequestBody SalonDTO salonDTO,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        if(userDTO == null){
            throw new Exception("User not found from JWT");
        }
        Salon updatedSalon = salonService.updateSalon(salonDTO,userDTO,salonId);
        SalonDTO responseSalonDTO = SalonMapper.mapSalonToDTO(updatedSalon);
        return new ResponseEntity<>(responseSalonDTO,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SalonDTO>> getAllSalons(){
        List<Salon> salons = salonService.getAllSalons();
        List<SalonDTO> salonDTOS = salons.stream().map(SalonMapper::mapSalonToDTO).toList();
        return ResponseEntity.ok(salonDTOS);
    }

    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId) throws Exception {
        Salon salon = salonService.getSalonById(salonId);
        SalonDTO responseSalonDTO = SalonMapper.mapSalonToDTO(salon);
        return ResponseEntity.ok(responseSalonDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalons(@RequestParam String city) throws Exception {
        List<Salon> salons = salonService.serachSalonByCityName(city);
        List<SalonDTO> salonDTOS = salons.stream().map(SalonMapper::mapSalonToDTO).toList();
        return ResponseEntity.ok(salonDTOS);
    }

    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        if(userDTO == null){
            throw new Exception("User not found from JWT");
        }
        Salon salon = salonService.getSalonByOwnerId(userDTO.getId());
        SalonDTO responseSalonDTO = SalonMapper.mapSalonToDTO(salon);
        return ResponseEntity.ok(responseSalonDTO);
    }

}
