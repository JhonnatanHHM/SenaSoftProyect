package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.PasajerosDTO;

import java.util.List;
import java.util.Optional;

public interface PasajerosService {

    List<PasajerosDTO> getAll();

    Optional<PasajerosDTO> getByIdPasajero(Long idPasajero);

    PasajerosDTO save(PasajerosDTO pasajero);

    PasajerosDTO update(PasajerosDTO pasajero);

    void delete(Long idPasajero);
}
