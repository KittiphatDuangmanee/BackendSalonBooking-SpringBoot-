package com.salonbooking.userservice.service.imp;

import com.salonbooking.userservice.modal.User;
import com.salonbooking.userservice.payload.dto.SignUpDTO;
import com.salonbooking.userservice.payload.response.AuthResponse;
import com.salonbooking.userservice.payload.response.TokenResponse;
import com.salonbooking.userservice.repository.UserRepository;
import com.salonbooking.userservice.service.AuthService;
import com.salonbooking.userservice.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    public AuthResponse login(String username, String password) throws Exception {
        TokenResponse tokenResponse = keycloakService.getAdminAccessToken(username,
                password,
                "password", null);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login successfully");
        return authResponse;
    }

    @Override
    public AuthResponse signup(SignUpDTO signUpDTO) throws Exception {
        keycloakService.createUser(signUpDTO);
        User user = new User();
        user.setUsername(signUpDTO.getUsername());
        user.setPassword(signUpDTO.getPassword());
        user.setEmail(signUpDTO.getEmail());
        user.setRole(signUpDTO.getRole());
        user.setFullName(signUpDTO.getFullName());
        user.setCreateAt(LocalDateTime.now());
        userRepository.save(user);

        TokenResponse tokenResponse = keycloakService.getAdminAccessToken(signUpDTO.getUsername(),
                signUpDTO.getPassword(),
                "password", null);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setRole(user.getRole());
        authResponse.setMessage("Register successfully");

        return authResponse;
    }

    @Override
    public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception {
        TokenResponse tokenResponse = keycloakService.getAdminAccessToken(
                null,
                null,
                "refresh_token", refreshToken);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Refresh token successfully");
        return authResponse;
    }
}
