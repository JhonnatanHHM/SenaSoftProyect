package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.JwtResponseDto;
import com.senasoftproyect.demo.application.dtos.LoginDto;
import com.senasoftproyect.demo.application.dtos.UsuariosDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UsuariosService {

    boolean existsById (Long idUsuario);

    Optional<UsuariosDTO> getByIdUsuario(Long idUsuario);

    UsuariosDTO register(UsuariosDTO registerDto);

    UsuariosDTO editUser(UsuariosDTO userDto);

    List<UsuariosDTO> getAll();

    void deleteUser(Long idUsuario) throws IOException;
}
