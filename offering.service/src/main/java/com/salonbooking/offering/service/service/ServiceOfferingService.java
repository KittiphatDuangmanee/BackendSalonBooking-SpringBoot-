package com.salonbooking.offering.service.service;

import com.salonbooking.offering.service.dto.CategoryDTO;
import com.salonbooking.offering.service.dto.SalonDTO;
import com.salonbooking.offering.service.dto.ServiceOfferingDTO;
import com.salonbooking.offering.service.modal.ServiceOffering;

import java.util.Set;

public interface ServiceOfferingService {

    ServiceOffering createServiceOffering(ServiceOfferingDTO serviceOfferingDTO,
                                          SalonDTO salonDTO,
                                          CategoryDTO categoryDTO);

    ServiceOffering updateServiceOffering (Long serviceOfferingId
                                           ,ServiceOffering serviceOffering) throws Exception;

    Set<ServiceOffering> getAllServiceOfferings(Long salonId, Long categoryId);

    Set<ServiceOffering> getServiceOfferings(Set<Long> serviceOfferingIds);

    ServiceOffering getServiceOfferingById(Long serviceOfferingId) throws Exception;
}
