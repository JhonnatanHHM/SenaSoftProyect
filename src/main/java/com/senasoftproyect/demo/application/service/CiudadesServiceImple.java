package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.CiudadesCompleteDTO;
import com.senasoftproyect.demo.application.dtos.CiudadesDTO;
import com.senasoftproyect.demo.domain.entitys.CiudadesEntity;
import com.senasoftproyect.demo.domain.entitys.RegionesEntity;
import com.senasoftproyect.demo.domain.repository.CiudadesRepository;
import com.senasoftproyect.demo.domain.repository.RegionesRepository;
import com.senasoftproyect.demo.domain.service.CiudadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CiudadesServiceImple implements CiudadesService {

    private final CiudadesRepository ciudadesRepository;
    private final RegionesRepository regionesRepository;

    @Autowired
    public CiudadesServiceImple(CiudadesRepository ciudadesRepository, RegionesRepository regionesRepository) {
        this.ciudadesRepository = ciudadesRepository;
        this.regionesRepository = regionesRepository;
    }

    @Override
    public List<CiudadesEntity> getAll() {
        return ciudadesRepository.getAll();
    }

    @Override
    public Optional<CiudadesEntity> getByIdCiudad(Long idCiudad) {
        return ciudadesRepository.getByIdCiudad(idCiudad);
    }

    @Override
    public CiudadesEntity save(CiudadesEntity ciudad) {
        return ciudadesRepository.save(ciudad);
    }

    @Override
    public CiudadesEntity update(CiudadesEntity ciudad) {
        Optional<CiudadesEntity> existing = ciudadesRepository.getByIdCiudad(ciudad.getIdCiudad());
        if (existing.isEmpty()) {
            throw new RuntimeException("Ciudad no encontrada con ID: " + ciudad.getIdCiudad());
        }

        CiudadesEntity current = existing.get();
        current.setNombre(ciudad.getNombre());
        current.setRegion(ciudad.getRegion());

        return ciudadesRepository.save(current);
    }

    @Override
    public void delete(Long idCiudad) {
        Optional<CiudadesEntity> ciudadOpt = ciudadesRepository.getByIdCiudad(idCiudad);
        if (ciudadOpt.isEmpty()) {
            throw new RuntimeException("Ciudad no encontrada con ID: " + idCiudad);
        }
        ciudadesRepository.delete(ciudadOpt.get().getIdCiudad());
    }

    public CiudadesDTO convertToDto(CiudadesEntity entity) {
        CiudadesDTO dto = new CiudadesDTO();
        dto.setIdCiudad(entity.getIdCiudad());
        dto.setNombre(entity.getNombre());
        if (entity.getRegion() != null) {
            dto.setIdRegion(entity.getRegion().getIdRegion());
            dto.setNombreRegion(entity.getRegion().getNombre());
        }
        return dto;
    }

    public CiudadesCompleteDTO convertToCompleteDto(CiudadesEntity entity) {
        CiudadesCompleteDTO dto = new CiudadesCompleteDTO();
        dto.setIdCiudad(entity.getIdCiudad());
        dto.setNombre(entity.getNombre());

        if (entity.getRegion() != null) {
            dto.setIdRegion(entity.getRegion().getIdRegion());
            dto.setNombreRegion(entity.getRegion().getNombre());

            if (entity.getRegion().getPais() != null) {
                dto.setNombrePais(entity.getRegion().getPais().getNombre());
            }
        }

        return dto;
    }

    public CiudadesEntity convertToEntity(CiudadesDTO dto) {
        CiudadesEntity entity = new CiudadesEntity();
        entity.setIdCiudad(dto.getIdCiudad());
        entity.setNombre(dto.getNombre());

        if (dto.getIdRegion() != null) {
            Optional<RegionesEntity> regionOpt = regionesRepository.getByIdRegion(dto.getIdRegion());
            regionOpt.ifPresent(entity::setRegion);
        }
        return entity;
    }

    public List<CiudadesCompleteDTO> getAllCompleteDto() {
        return ciudadesRepository.getAll()
                .stream()
                .map(this::convertToCompleteDto)
                .collect(Collectors.toList());
    }

    public Optional<CiudadesCompleteDTO> getCompleteById(Long idCiudad) {
        return ciudadesRepository.getByIdCiudad(idCiudad)
                .map(this::convertToCompleteDto);
    }
}
