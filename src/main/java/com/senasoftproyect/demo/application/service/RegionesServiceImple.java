package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.RegionesDTO;
import com.senasoftproyect.demo.domain.entitys.PaisesEntity;
import com.senasoftproyect.demo.domain.entitys.RegionesEntity;
import com.senasoftproyect.demo.domain.repository.PaisesRepository;
import com.senasoftproyect.demo.domain.repository.RegionesRepository;
import com.senasoftproyect.demo.domain.service.RegionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionesServiceImple implements RegionesService {

    private final RegionesRepository regionesRepository;
    private final PaisesRepository paisesRepository;

    @Autowired
    public RegionesServiceImple(RegionesRepository regionesRepository, PaisesRepository paisesRepository) {
        this.regionesRepository = regionesRepository;
        this.paisesRepository = paisesRepository;
    }

    @Override
    public List<RegionesEntity> getAll() {
        return regionesRepository.getAll();
    }

    @Override
    public Optional<RegionesEntity> getByIdRegion(Long idRegion) {
        return regionesRepository.getByIdRegion(idRegion);
    }

    @Override
    public RegionesEntity save(RegionesEntity region) {
        return regionesRepository.save(region);
    }

    @Override
    public RegionesEntity update(RegionesEntity region) {
        Optional<RegionesEntity> existing = regionesRepository.getByIdRegion(region.getIdRegion());
        if (existing.isEmpty()) {
            throw new RuntimeException("Región no encontrada con ID: " + region.getIdRegion());
        }

        RegionesEntity current = existing.get();
        current.setNombre(region.getNombre());
        current.setPais(region.getPais());

        return regionesRepository.save(current);
    }

    @Override
    public void delete(Long idRegion) {
        Optional<RegionesEntity> regionOpt = regionesRepository.getByIdRegion(idRegion);
        if (regionOpt.isEmpty()) {
            throw new RuntimeException("Región no encontrada con ID: " + idRegion);
        }
        regionesRepository.delete(regionOpt.get().getIdRegion());
    }

    public RegionesDTO convertToDto(RegionesEntity entity) {
        RegionesDTO dto = new RegionesDTO();
        dto.setIdRegion(entity.getIdRegion());
        dto.setNombre(entity.getNombre());
        if (entity.getPais() != null) {
            dto.setIdPais(entity.getPais().getIdPais());
            dto.setNombrePais(entity.getPais().getNombre());
        }
        return dto;
    }

    public RegionesEntity convertToEntity(RegionesDTO dto) {
        RegionesEntity entity = new RegionesEntity();
        entity.setIdRegion(dto.getIdRegion());
        entity.setNombre(dto.getNombre());

        if (dto.getIdPais() != null) {
            Optional<PaisesEntity> paisOpt = paisesRepository.getByIdPais(dto.getIdPais());
            paisOpt.ifPresent(entity::setPais);
        }
        return entity;
    }

    public List<RegionesDTO> getAllAsDto() {
        return regionesRepository.getAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
