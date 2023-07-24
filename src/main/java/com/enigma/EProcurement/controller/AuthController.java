package com.enigma.EProcurement.controller;

import com.enigma.EProcurement.model.request.AuthRequest;
import com.enigma.EProcurement.model.request.RegisterAdminRequest;
import com.enigma.EProcurement.model.response.CommonResponse;
import com.enigma.EProcurement.model.response.LoginResponse;
import com.enigma.EProcurement.model.response.RegisterResponse;
import com.enigma.EProcurement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterAdminRequest request) {
        RegisterResponse register = authService.registerAdmin(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully registered")
                .data(register)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commonResponse);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        LoginResponse response = authService.login(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully login")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(commonResponse);
    }

}
