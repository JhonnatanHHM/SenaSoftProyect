package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.AsientosDTO;
import com.senasoftproyect.demo.domain.entitys.AsientosEntity;
import com.senasoftproyect.demo.domain.repository.AsientosRepository;
import com.senasoftproyect.demo.domain.repository.UsuariosRepository;
import com.senasoftproyect.demo.domain.service.AsientosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsientosServiceImple implements AsientosService {

    private final UsuariosRepository usuariosRepository;
    private final AsientosRepository asientosRepository;

    @Autowired
    public AsientosServiceImple(UsuariosRepository usuariosRepository, AsientosRepository asientosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.asientosRepository = asientosRepository;
    }

    @Override
    public List<AsientosDTO> getAll() {
        return asientosRepository.getAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AsientosDTO> getByIdAsiento(Long idAsiento) {
        return asientosRepository.getByIdAsiento(idAsiento)
                .map(this::convertToDto);
    }

    @Override
    public AsientosDTO save(AsientosDTO dto) {
        AsientosEntity entity = convertToEntity(dto);
        AsientosEntity saved = asientosRepository.save(entity);
        return convertToDto(saved);
    }

    @Override
    public AsientosDTO update(AsientosDTO dto) {
        if (dto.getIdAsiento() == null) {
            throw new IllegalArgumentException("El ID del asiento es obligatorio para actualizar.");
        }
        AsientosEntity entity = convertToEntity(dto);
        AsientosEntity updated = asientosRepository.update(entity);
        return convertToDto(updated);
    }

    @Override
    public void delete(Long idAsiento) {
        asientosRepository.delete(idAsiento);
    }

    @Transactional
    @Override
    public AsientosDTO actualizarEstado(Long idAsiento, Long idUsuario) {

        AsientosEntity asiento = asientosRepository.getByIdAsiento(idAsiento)
                .orElseThrow(() -> new RuntimeException("Asiento no encontrado con id " + idAsiento));

        switch (asiento.getEstado()) {
            case OCUPADO:
                throw new RuntimeException("El asiento ya está ocupado y no se puede modificar.");

            case SELECCIONADO:
                asiento.setEstado(AsientosEntity.AsientoStatus.DISPONIBLE);
                asiento.setUsuarioReservado(null);
                break;

            case DISPONIBLE:
                asiento.setEstado(AsientosEntity.AsientoStatus.SELECCIONADO);
                asiento.setUsuarioReservado(
                        usuariosRepository.getByIdUsuario(idUsuario)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + idUsuario))
                                .getIdUsuario()
                );
                break;

            default:
                throw new RuntimeException("Estado del asiento no reconocido: " + asiento.getEstado());
        }

        AsientosEntity actualizado = asientosRepository.save(asiento);
        return convertToDto(actualizado);
    }

    private AsientosDTO convertToDto(AsientosEntity entity) {
        AsientosDTO dto = new AsientosDTO();
        dto.setIdAsiento(entity.getIdAsiento());
        dto.setNombre(entity.getNombre());
        dto.setPrecio(entity.getPrecio());
        dto.setEstado(entity.getEstado());
        if (entity.getAvion() != null) {
            dto.setIdAvion(entity.getAvion().getIdAvion());
        }
        dto.setUsuarioReservado(entity.getUsuarioReservado());
        return dto;
    }

    private AsientosEntity convertToEntity(AsientosDTO dto) {
        AsientosEntity entity = new AsientosEntity();
        entity.setIdAsiento(dto.getIdAsiento());
        entity.setNombre(dto.getNombre());
        entity.setPrecio(dto.getPrecio());
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }
        entity.setUsuarioReservado(dto.getUsuarioReservado());
        return entity;
    }
}
