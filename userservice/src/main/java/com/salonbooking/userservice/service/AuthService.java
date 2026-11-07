package com.salonbooking.userservice.service;

import com.salonbooking.userservice.payload.dto.SignUpDTO;
import com.salonbooking.userservice.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username, String password) throws Exception;
    AuthResponse signup(SignUpDTO signUpDTO) throws Exception;
    AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception;
}
