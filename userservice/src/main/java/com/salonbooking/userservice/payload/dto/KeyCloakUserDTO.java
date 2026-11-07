package com.salonbooking.userservice.payload.dto;

import lombok.Data;

@Data
public class KeyCloakUserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
