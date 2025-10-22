package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.PasajerosEntity;
import com.senasoftproyect.demo.domain.repository.PasajerosRepository;
import com.senasoftproyect.demo.domain.repository.crud.PasajerosCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PasajerosRepositoryImple implements PasajerosRepository {

    private final PasajerosCrudRepository pasajerosCrudRepository;

    @Autowired
    public PasajerosRepositoryImple(PasajerosCrudRepository pasajerosCrudRepository) {
        this.pasajerosCrudRepository = pasajerosCrudRepository;
    }

    @Override
    public List<PasajerosEntity> getAll() {
        return pasajerosCrudRepository.findAll();
    }

    @Override
    public void delete(Long idPasajero) {
        pasajerosCrudRepository.deleteById(idPasajero);
    }

    @Override
    public PasajerosEntity update(PasajerosEntity pasajero) {
        return pasajerosCrudRepository.saveAndFlush(pasajero);
    }

    @Override
    public PasajerosEntity save(PasajerosEntity pasajero) {
        return pasajerosCrudRepository.saveAndFlush(pasajero);
    }

    @Override
    public Optional<PasajerosEntity> getByIdPasajero(Long idPasajero) {
        return pasajerosCrudRepository.findById(idPasajero);
    }
}
