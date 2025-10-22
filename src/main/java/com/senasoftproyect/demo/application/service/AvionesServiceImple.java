package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.AvionesDTO;
import com.senasoftproyect.demo.application.dtos.AsientosDTO;
import com.senasoftproyect.demo.domain.entitys.AvionesEntity;
import com.senasoftproyect.demo.domain.repository.AvionesRepository;
import com.senasoftproyect.demo.domain.service.AvionesService;
import com.senasoftproyect.demo.domain.service.AsientosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvionesServiceImple implements AvionesService {

    private final AvionesRepository avionesRepository;
    private final AsientosService asientosService;

    @Autowired
    public AvionesServiceImple(AvionesRepository avionesRepository, AsientosService asientosService) {
        this.avionesRepository = avionesRepository;
        this.asientosService = asientosService;
    }

    @Override
    public List<AvionesDTO> getAll() {
        return avionesRepository.getAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AvionesDTO> getByIdAvion(Long idAvion) {
        return avionesRepository.getByIdAvion(idAvion)
                .map(this::convertToDto);
    }

    @Override
    public AvionesDTO save(AvionesDTO avionDto) {

        AvionesEntity avionEntity = convertToEntity(avionDto);
        AvionesEntity savedAvion = avionesRepository.save(avionEntity);

        if (avionDto.getAsientos() != null) {
            List<AsientosDTO> asientos = avionDto.getAsientos();
            for (AsientosDTO asientoDto : asientos) {
                asientoDto.setIdAvion(savedAvion.getIdAvion());
                asientosService.save(asientoDto);
            }
        }

        return convertToDto(savedAvion);
    }

    @Override
    public AvionesDTO update(AvionesDTO avionDto) {
        AvionesEntity avionEntity = convertToEntity(avionDto);
        AvionesEntity updatedAvion = avionesRepository.update(avionEntity);

        if (avionDto.getAsientos() != null) {
            for (AsientosDTO asientoDto : avionDto.getAsientos()) {
                asientoDto.setIdAvion(updatedAvion.getIdAvion());
                asientosService.update(asientoDto);
            }
        }

        return convertToDto(updatedAvion);
    }

    @Override
    public void delete(Long idAvion) {
        avionesRepository.delete(idAvion);
    }


    private AvionesDTO convertToDto(AvionesEntity entity) {
        AvionesDTO dto = new AvionesDTO();
        dto.setIdAvion(entity.getIdAvion());
        dto.setModelo(entity.getModelo());
        dto.setCapacidad(entity.getCapacidad());

        if (entity.getAsientos() != null) {
            List<AsientosDTO> asientosDto = entity.getAsientos()
                    .stream()
                    .map(asiento -> {
                        AsientosDTO dtoAsiento = new AsientosDTO();
                        dtoAsiento.setIdAsiento(asiento.getIdAsiento());
                        dtoAsiento.setNombre(asiento.getNombre());
                        dtoAsiento.setPrecio(asiento.getPrecio());
                        dtoAsiento.setEstado(asiento.getEstado());
                        dtoAsiento.setIdAvion(entity.getIdAvion());
                        return dtoAsiento;
                    }).collect(Collectors.toList());
            dto.setAsientos(asientosDto);
        }

        return dto;
    }

    private AvionesEntity convertToEntity(AvionesDTO dto) {
        AvionesEntity entity = new AvionesEntity();
        entity.setIdAvion(dto.getIdAvion());
        entity.setModelo(dto.getModelo());
        entity.setCapacidad(dto.getCapacidad());

        return entity;
    }
}
