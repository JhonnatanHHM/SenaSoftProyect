package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.AsientosEntity;
import com.senasoftproyect.demo.domain.repository.AsientosRepository;
import com.senasoftproyect.demo.domain.repository.crud.AsientosCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AsientosRepositoryImple implements AsientosRepository {

    @Override
    public List<AsientosEntity> saveAll(List<AsientosEntity> asientos) {
        return asientosCrudRepository.saveAll(asientos);
    }

    private final AsientosCrudRepository asientosCrudRepository;

    @Autowired
    public AsientosRepositoryImple(AsientosCrudRepository asientosCrudRepository) {
        this.asientosCrudRepository = asientosCrudRepository;
    }

    @Override
    public List<AsientosEntity> getAll() {
        return asientosCrudRepository.findAll();
    }

    @Override
    public void delete(Long idAsiento) {
        asientosCrudRepository.deleteById(idAsiento);
    }

    @Override
    public AsientosEntity update(AsientosEntity asiento) {
        return asientosCrudRepository.saveAndFlush(asiento);
    }

    @Override
    public AsientosEntity save(AsientosEntity asiento) {
        return asientosCrudRepository.saveAndFlush(asiento);
    }

    @Override
    public Optional<AsientosEntity> getByIdAsiento(Long idAsiento) {
        return asientosCrudRepository.findById(idAsiento);
    }

}
