package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.JwtResponseDto;

import com.senasoftproyect.demo.domain.service.AuthService;
import com.senasoftproyect.demo.infrastructure.exceptions.JwtAuthenticationException;
import com.senasoftproyect.demo.infrastructure.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.senasoftproyect.demo.application.dtos.LoginDto;

@Service
public class AuthServiceImple implements AuthService {

    private JwtGenerator jwtGenerator;

    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImple(JwtGenerator jwtGenerator, AuthenticationManager authenticationManager) {
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtResponseDto login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return new JwtResponseDto(token);
        } catch (AuthenticationException e) {
            throw new JwtAuthenticationException("Credenciales inválidas");
        }
    }
}
