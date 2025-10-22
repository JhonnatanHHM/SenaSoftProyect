package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.RegionesEntity;
import com.senasoftproyect.demo.domain.repository.RegionesRepository;
import com.senasoftproyect.demo.domain.repository.crud.RegionesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RegionesRepositoryImple implements RegionesRepository {

    private final RegionesCrudRepository regionesCrudRepository;

    @Autowired
    public RegionesRepositoryImple(RegionesCrudRepository regionesCrudRepository) {
        this.regionesCrudRepository = regionesCrudRepository;
    }

    @Override
    public List<RegionesEntity> getAll() {
        return regionesCrudRepository.findAll();
    }

    @Override
    public void delete(Long idRegion) {
        regionesCrudRepository.deleteById(idRegion);
    }

    @Override
    public RegionesEntity update(RegionesEntity region) {
        return regionesCrudRepository.saveAndFlush(region);
    }

    @Override
    public RegionesEntity save(RegionesEntity region) {
        return regionesCrudRepository.saveAndFlush(region);
    }

    @Override
    public Optional<RegionesEntity> getByIdRegion(Long idRegion) {
        return regionesCrudRepository.findById(idRegion);
    }
}
