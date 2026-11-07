package com.salonbooking.userservice.payload.dto;

import com.salonbooking.userservice.domain.UserRole;
import lombok.Data;

@Data
public class SignUpDTO {
    private String fullName;
    private String email;
    private String password;
    private String username;
    private UserRole role;
}
