package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.PasajerosEntity;

import java.util.List;
import java.util.Optional;

public interface PasajerosService {

    List<PasajerosEntity> getAll();

    Optional<PasajerosEntity> getByIdPasajero(Long idPasajero);

    PasajerosEntity save(PasajerosEntity pasajero);

    PasajerosEntity update(PasajerosEntity pasajero);

    void delete(Long idPasajero);
}
