package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.domain.entitys.UsuariosEntity;
import com.senasoftproyect.demo.domain.repository.UsuariosRepository;
import com.senasoftproyect.demo.domain.service.AuthenticatedService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailService")
@Transactional(readOnly = true)
public class AuthenticatedServiceImple implements AuthenticatedService {

    private UsuariosRepository usuariosRepository;

    @Autowired
    public AuthenticatedServiceImple(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsuariosEntity user = usuariosRepository.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Propietario no encontrado!"));

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("Nombre" + " " + user.getNombres()));

        authorities.add(new SimpleGrantedAuthority("Telefono" + " " + user.getCelular()));

        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
