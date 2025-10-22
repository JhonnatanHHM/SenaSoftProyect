package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.UsuariosDTO;
import com.senasoftproyect.demo.domain.entitys.UsuariosEntity;
import com.senasoftproyect.demo.domain.repository.UsuariosRepository;
import com.senasoftproyect.demo.domain.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuariosServiceImple implements UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuariosServiceImple(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existsById(Long idUsuario) {
        return usuariosRepository.existsById(idUsuario);
    }

    @Override
    public Optional<UsuariosDTO> getByIdUsuario(Long idUsuario) {
        return usuariosRepository.getByIdUsuario(idUsuario)
                .map(this::convertToDto);
    }

    @Override
    public UsuariosDTO register(UsuariosDTO registerDto) {
        UsuariosEntity entity = convertToEntity(registerDto);
        entity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        entity.setEstado(true);

        UsuariosEntity saved = usuariosRepository.save(entity);
        return convertToDto(saved);
    }

    @Override
    public UsuariosDTO editUser(UsuariosDTO userDto) {
        Optional<UsuariosEntity> existingUserOpt = usuariosRepository.getByIdUsuario(userDto.getIdUsuario());

        if (existingUserOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userDto.getIdUsuario());
        }

        UsuariosEntity existingUser = existingUserOpt.get();

        existingUser.setNombres(userDto.getNombres());
        existingUser.setPrimerApellido(userDto.getPrimerApellido());
        existingUser.setSegundoApellido(userDto.getSegundoApellido());
        existingUser.setCelular(userDto.getCelular());
        existingUser.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        UsuariosEntity updated = usuariosRepository.save(existingUser);
        return convertToDto(updated);
    }

    @Override
    public List<UsuariosDTO> getAll() {
        return usuariosRepository.getAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long idUsuario) throws IOException {
        if (!usuariosRepository.existsById(idUsuario)) {
            throw new IOException("Usuario no encontrado con ID: " + idUsuario);
        }
        usuariosRepository.delete(idUsuario);
    }


    private UsuariosDTO convertToDto(UsuariosEntity entity) {
        UsuariosDTO dto = new UsuariosDTO();
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setEstado(entity.isEstado());
        dto.setNombres(entity.getNombres());
        dto.setPrimerApellido(entity.getPrimerApellido());
        dto.setSegundoApellido(entity.getSegundoApellido());
        dto.setCelular(entity.getCelular());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    private UsuariosEntity convertToEntity(UsuariosDTO dto) {
        UsuariosEntity entity = new UsuariosEntity();
        entity.setIdUsuario(dto.getIdUsuario());
        entity.setFechaRegistro(dto.getFechaRegistro());
        entity.setEstado(dto.isEstado());
        entity.setNombres(dto.getNombres());
        entity.setPrimerApellido(dto.getPrimerApellido());
        entity.setSegundoApellido(dto.getSegundoApellido());
        entity.setCelular(dto.getCelular());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        return entity;
    }
}
