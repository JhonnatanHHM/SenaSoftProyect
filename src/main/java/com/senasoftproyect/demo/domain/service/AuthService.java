package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.JwtResponseDto;
import com.senasoftproyect.demo.application.dtos.LoginDto;

public interface AuthService {
    JwtResponseDto login(LoginDto loginDto);
}
