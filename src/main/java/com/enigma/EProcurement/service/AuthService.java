package com.enigma.EProcurement.service;

import com.enigma.EProcurement.model.request.RegisterAdminRequest;
import com.enigma.EProcurement.model.request.AuthRequest;
import com.enigma.EProcurement.model.response.LoginResponse;
import com.enigma.EProcurement.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerAdmin(RegisterAdminRequest request);
    LoginResponse login(AuthRequest request);

}
