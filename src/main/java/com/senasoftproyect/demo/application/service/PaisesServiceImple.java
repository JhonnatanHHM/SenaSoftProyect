package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.PaisesDTO;
import com.senasoftproyect.demo.domain.entitys.PaisesEntity;
import com.senasoftproyect.demo.domain.repository.PaisesRepository;
import com.senasoftproyect.demo.domain.service.PaisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaisesServiceImple implements PaisesService {

    private final PaisesRepository paisesRepository;

    @Autowired
    public PaisesServiceImple(PaisesRepository paisesRepository) {
        this.paisesRepository = paisesRepository;
    }

    @Override
    public List<PaisesEntity> getAll() {
        return paisesRepository.getAll();
    }

    @Override
    public Optional<PaisesEntity> getByIdPais(Long idPais) {
        return paisesRepository.getByIdPais(idPais);
    }

    @Override
    public PaisesEntity save(PaisesEntity pais) {
        return paisesRepository.save(pais);
    }

    @Override
    public PaisesEntity update(PaisesEntity pais) {
        Optional<PaisesEntity> existing = paisesRepository.getByIdPais(pais.getIdPais());
        if (existing.isEmpty()) {
            throw new RuntimeException("País no encontrado con ID: " + pais.getIdPais());
        }

        PaisesEntity current = existing.get();
        current.setNombre(pais.getNombre());

        return paisesRepository.save(current);
    }

    @Override
    public void delete(Long idPais) {
        Optional<PaisesEntity> paisOpt = paisesRepository.getByIdPais(idPais);
        if (paisOpt.isEmpty()) {
            throw new RuntimeException("País no encontrado con ID: " + idPais);
        }
        paisesRepository.delete(paisOpt.get().getIdPais());
    }


    public PaisesDTO convertToDto(PaisesEntity entity) {
        PaisesDTO dto = new PaisesDTO();
        dto.setIdPais(entity.getIdPais());
        dto.setNombre(entity.getNombre());
        return dto;
    }

    public PaisesEntity convertToEntity(PaisesDTO dto) {
        PaisesEntity entity = new PaisesEntity();
        entity.setIdPais(dto.getIdPais());
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public List<PaisesDTO> getAllAsDto() {
        return paisesRepository.getAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
