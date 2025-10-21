package com.senasoftproyect.demo.domain.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticatedService extends UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
