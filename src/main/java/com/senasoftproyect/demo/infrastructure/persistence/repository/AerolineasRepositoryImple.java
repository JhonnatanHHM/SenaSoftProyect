package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.AerolineasEntity;
import com.senasoftproyect.demo.domain.repository.AerolineasRepository;
import com.senasoftproyect.demo.domain.repository.crud.AerolineasCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AerolineasRepositoryImple implements AerolineasRepository {

    private final AerolineasCrudRepository aerolineasCrudRepository;

    @Autowired
    public AerolineasRepositoryImple(AerolineasCrudRepository aerolineasCrudRepository) {
        this.aerolineasCrudRepository = aerolineasCrudRepository;
    }

    @Override
    public List<AerolineasEntity> getAll() {
        return aerolineasCrudRepository.findAll();
    }

    @Override
    public void delete(Long idAerolinea) {
        aerolineasCrudRepository.deleteById(idAerolinea);
    }

    @Override
    public AerolineasEntity update(AerolineasEntity aerolinea) {
        return aerolineasCrudRepository.saveAndFlush(aerolinea);
    }

    @Override
    public AerolineasEntity save(AerolineasEntity aerolinea) {
        return aerolineasCrudRepository.saveAndFlush(aerolinea);
    }

    @Override
    public Optional<AerolineasEntity> getByIdAerolinea(Long idAerolinea) {
        return aerolineasCrudRepository.findById(idAerolinea);
    }
}
