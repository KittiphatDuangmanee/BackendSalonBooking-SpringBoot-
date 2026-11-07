package com.salonbooking.offering.service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ServiceOfferingDTO {

    private Long id;

    private String name;

    private String description;

    private int price;

    private int duration;

    private Long salonId;

    private Long categoryId;

    private String image;
}
