package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.CiudadesEntity;
import com.senasoftproyect.demo.domain.repository.CiudadesRepository;
import com.senasoftproyect.demo.domain.repository.crud.CiudadesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CiudadesRepositoryImple implements CiudadesRepository {

    private final CiudadesCrudRepository ciudadesCrudRepository;

    @Autowired
    public CiudadesRepositoryImple(CiudadesCrudRepository ciudadesCrudRepository) {
        this.ciudadesCrudRepository = ciudadesCrudRepository;
    }

    @Override
    public List<CiudadesEntity> getAll() {
        return ciudadesCrudRepository.findAll();
    }

    @Override
    public void delete(Long idCiudad) {
        ciudadesCrudRepository.deleteById(idCiudad);
    }

    @Override
    public CiudadesEntity update(CiudadesEntity ciudad) {
        return ciudadesCrudRepository.saveAndFlush(ciudad);
    }

    @Override
    public CiudadesEntity save(CiudadesEntity ciudad) {
        return ciudadesCrudRepository.saveAndFlush(ciudad);
    }

    @Override
    public Optional<CiudadesEntity> getByIdCiudad(Long idCiudad) {
        return ciudadesCrudRepository.findById(idCiudad);
    }
}
