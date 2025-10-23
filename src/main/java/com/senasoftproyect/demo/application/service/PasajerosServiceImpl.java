package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.PasajerosDTO;
import com.senasoftproyect.demo.domain.entitys.AsientosEntity;
import com.senasoftproyect.demo.domain.entitys.PasajerosEntity;
import com.senasoftproyect.demo.domain.repository.AsientosRepository;
import com.senasoftproyect.demo.domain.repository.PasajerosRepository;
import com.senasoftproyect.demo.domain.service.PasajerosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PasajerosServiceImpl implements PasajerosService {

    private PasajerosRepository pasajerosRepository;

    private AsientosRepository asientosRepository;

    @Autowired
    public PasajerosServiceImpl(PasajerosRepository pasajerosRepository, AsientosRepository asientosRepository) {
        this.pasajerosRepository = pasajerosRepository;
        this.asientosRepository = asientosRepository;
    }



    @Override
    public List<PasajerosDTO> getAll() {
        return pasajerosRepository.getAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<PasajerosDTO> getByIdPasajero(Long idPasajero) {
        return pasajerosRepository.getByIdPasajero(idPasajero).map(this::mapToDTO);
    }

    @Override
    public PasajerosDTO save(PasajerosDTO pasajero) {
        PasajerosEntity entity = mapToEntity(pasajero);
        return mapToDTO(pasajerosRepository.save(entity));
    }

    @Override
    public PasajerosDTO update(PasajerosDTO pasajero) {
        if (pasajero.getIdPasajero() == null) {
            throw new IllegalArgumentException("El ID del pasajero no puede ser null para actualizar");
        }
        PasajerosEntity entity = mapToEntity(pasajero);
        return mapToDTO(pasajerosRepository.save(entity));
    }

    @Override
    public void delete(Long idPasajero) {
        pasajerosRepository.delete(idPasajero);
    }

    private PasajerosDTO mapToDTO(PasajerosEntity entity) {
        Long idAsiento = entity.getAsiento() != null ? entity.getAsiento().getIdAsiento() : null;
        return new PasajerosDTO(
                entity.getId_pasajero(),
                entity.getNombres(),
                entity.getPrimerApellido(),
                entity.getSegundoApellido(),
                entity.getFechaNacimiento(),
                entity.getGenero(),
                entity.getNumeroDocumento(),
                entity.isInfante(),
                entity.getCelular(),
                entity.getEmail(),
                idAsiento
        );
    }

    private PasajerosEntity mapToEntity(PasajerosDTO dto) {
        AsientosEntity asiento = null;
        if (dto.getIdAsiento() != null) {
            asiento = asientosRepository.getByIdAsiento(dto.getIdAsiento()).orElse(null);
        }
        return new PasajerosEntity(
                dto.getIdPasajero(),
                dto.getNombres(),
                dto.getPrimerApellido(),
                dto.getSegundoApellido(),
                dto.getFechaNacimiento(),
                dto.getGenero(),
                dto.getNumeroDocumento(),
                dto.isInfante(),
                dto.getCelular(),
                dto.getEmail(),
                asiento
        );
    }
}
