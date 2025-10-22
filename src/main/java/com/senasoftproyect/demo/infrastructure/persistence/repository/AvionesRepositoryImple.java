package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.AvionesEntity;
import com.senasoftproyect.demo.domain.repository.AvionesRepository;
import com.senasoftproyect.demo.domain.repository.crud.AvionesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AvionesRepositoryImple implements AvionesRepository {

    private final AvionesCrudRepository avionesCrudRepository;

    @Autowired
    public AvionesRepositoryImple(AvionesCrudRepository avionesCrudRepository) {
        this.avionesCrudRepository = avionesCrudRepository;
    }

    @Override
    public List<AvionesEntity> getAll() {
        return avionesCrudRepository.findAll();
    }

    @Override
    public void delete(Long idAvion) {
        avionesCrudRepository.deleteById(idAvion);
    }

    @Override
    public AvionesEntity update(AvionesEntity avion) {
        return avionesCrudRepository.saveAndFlush(avion);
    }

    @Override
    public AvionesEntity save(AvionesEntity avion) {
        return avionesCrudRepository.saveAndFlush(avion);
    }

    @Override
    public Optional<AvionesEntity> getByIdAvion(Long idAvion) {
        return avionesCrudRepository.findById(idAvion);
    }
}
