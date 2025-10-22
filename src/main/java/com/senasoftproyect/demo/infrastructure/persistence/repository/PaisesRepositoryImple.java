package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.PaisesEntity;
import com.senasoftproyect.demo.domain.repository.PaisesRepository;
import com.senasoftproyect.demo.domain.repository.crud.PaisesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaisesRepositoryImple implements PaisesRepository {

    private final PaisesCrudRepository paisesCrudRepository;

    @Autowired
    public PaisesRepositoryImple(PaisesCrudRepository paisesCrudRepository) {
        this.paisesCrudRepository = paisesCrudRepository;
    }

    @Override
    public List<PaisesEntity> getAll() {
        return paisesCrudRepository.findAll();
    }

    @Override
    public void delete(Long idPais) {
        paisesCrudRepository.deleteById(idPais);
    }

    @Override
    public PaisesEntity update(PaisesEntity pais) {
        return paisesCrudRepository.saveAndFlush(pais);
    }

    @Override
    public PaisesEntity save(PaisesEntity pais) {
        return paisesCrudRepository.saveAndFlush(pais);
    }

    @Override
    public Optional<PaisesEntity> getByIdPais(Long idPais) {
        return paisesCrudRepository.findById(idPais);
    }
}
