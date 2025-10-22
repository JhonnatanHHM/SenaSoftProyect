package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.AsientosDTO;
import com.senasoftproyect.demo.domain.entitys.AsientosEntity;
import com.senasoftproyect.demo.domain.repository.AsientosRepository;
import com.senasoftproyect.demo.domain.service.AsientosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsientosServiceImple implements AsientosService {

    private final AsientosRepository asientosRepository;

    @Autowired
    public AsientosServiceImple(AsientosRepository asientosRepository) {
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

    private AsientosDTO convertToDto(AsientosEntity entity) {
        AsientosDTO dto = new AsientosDTO();
        dto.setIdAsiento(entity.getIdAsiento());
        dto.setNombre(entity.getNombre());
        dto.setPrecio(entity.getPrecio());
        dto.setEstado(entity.getEstado().name());
        if (entity.getAvion() != null) {
            dto.setIdAvion(entity.getAvion().getIdAvion());
        }
        return dto;
    }

    private AsientosEntity convertToEntity(AsientosDTO dto) {
        AsientosEntity entity = new AsientosEntity();
        entity.setIdAsiento(dto.getIdAsiento());
        entity.setNombre(dto.getNombre());
        entity.setPrecio(dto.getPrecio());
        if (dto.getEstado() != null) {
            entity.setEstado(AsientosEntity.AsientoStatus.valueOf(dto.getEstado()));
        }
        return entity;
    }
}
