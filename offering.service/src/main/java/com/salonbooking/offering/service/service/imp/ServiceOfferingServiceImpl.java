package com.salonbooking.offering.service.service.imp;

import com.salonbooking.offering.service.dto.CategoryDTO;
import com.salonbooking.offering.service.dto.SalonDTO;
import com.salonbooking.offering.service.dto.ServiceOfferingDTO;
import com.salonbooking.offering.service.modal.ServiceOffering;
import com.salonbooking.offering.service.repository.ServiceOfferingRepository;
import com.salonbooking.offering.service.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createServiceOffering(ServiceOfferingDTO serviceOfferingDTO,
                                                 SalonDTO salonDTO,
                                                 CategoryDTO categoryDTO) {
        ServiceOffering serviceOffering =  new ServiceOffering();
        serviceOffering.setName(serviceOfferingDTO.getName());
        serviceOffering.setDescription(serviceOfferingDTO.getDescription());
        serviceOffering.setPrice(serviceOfferingDTO.getPrice());
        serviceOffering.setDuration(serviceOfferingDTO.getDuration());
        serviceOffering.setSalonId(salonDTO.getId());
        serviceOffering.setCategoryId(categoryDTO.getId());
        serviceOffering.setImage(serviceOfferingDTO.getImage());

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateServiceOffering(Long serviceOfferingId,
                                                 ServiceOffering serviceOffering) throws Exception {
        ServiceOffering exitsServiceOffering = serviceOfferingRepository
                .findById(serviceOfferingId)
                .orElse(null);
        if (exitsServiceOffering == null) {
            throw new Exception("ServiceOffering not exits with id " + serviceOfferingId);
        }
        exitsServiceOffering.setName(serviceOffering.getName());
        exitsServiceOffering.setDescription(serviceOffering.getDescription());
        exitsServiceOffering.setPrice(serviceOffering.getPrice());
        exitsServiceOffering.setDuration(serviceOffering.getDuration());
        exitsServiceOffering.setImage(serviceOffering.getImage());

        return serviceOfferingRepository.save(exitsServiceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServiceOfferings(Long salonId, Long categoryId) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingRepository.findBySalonId(salonId);

        if(categoryId != null){
           serviceOfferings = serviceOfferings.stream().filter(serviceOffering -> serviceOffering.getCategoryId() != null
                    && serviceOffering.getCategoryId().equals(categoryId)).collect(Collectors.toSet());
        }
        return serviceOfferings;

    }

    @Override
    public Set<ServiceOffering> getServiceOfferings(Set<Long> serviceOfferingIds) {
        List<ServiceOffering> serviceOfferingList = serviceOfferingRepository.findAllById(serviceOfferingIds);

        return new HashSet<>(serviceOfferingList);
    }

    @Override
    public ServiceOffering getServiceOfferingById(Long serviceOfferingId) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceOfferingId)
                .orElse(null);
        if(serviceOffering == null){
            throw new Exception("ServiceOffering not exits with id " + serviceOfferingId);
        }
        return serviceOffering;
    }
}
