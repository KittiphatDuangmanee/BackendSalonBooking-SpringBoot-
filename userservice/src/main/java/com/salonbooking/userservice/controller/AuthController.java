package com.salonbooking.userservice.controller;

import com.salonbooking.userservice.payload.dto.LoginDTO;
import com.salonbooking.userservice.payload.dto.SignUpDTO;
import com.salonbooking.userservice.payload.response.AuthResponse;
import com.salonbooking.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignUpDTO request) throws Exception {
        AuthResponse authResponse = authService.signup(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) throws Exception {
        AuthResponse authResponse = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/access-token/refresh-token/{refreshToken}")
    public ResponseEntity<AuthResponse> getAccessToken(@PathVariable String refreshToken) throws Exception {
        AuthResponse authResponse = authService.getAccessTokenFromRefreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }
}
