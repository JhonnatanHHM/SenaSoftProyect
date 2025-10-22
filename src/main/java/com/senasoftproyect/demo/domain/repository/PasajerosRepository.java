package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.PasajerosEntity;
import java.util.List;
import java.util.Optional;

public interface PasajerosRepository {

    List<PasajerosEntity> getAll();

    void delete(Long idPasajero);

    PasajerosEntity update(PasajerosEntity pasajero);

    PasajerosEntity save(PasajerosEntity pasajero);

    Optional<PasajerosEntity> getByIdPasajero(Long idPasajero);
}
