package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.VuelosEntity;
import com.senasoftproyect.demo.domain.repository.VuelosRepository;
import com.senasoftproyect.demo.domain.repository.crud.VuelosCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VuelosRepositoryImple implements VuelosRepository {

    private final VuelosCrudRepository vuelosCrudRepository;

    @Autowired
    public VuelosRepositoryImple(VuelosCrudRepository vuelosCrudRepository) {
        this.vuelosCrudRepository = vuelosCrudRepository;
    }

    @Override
    public List<VuelosEntity> getAll() {
        return vuelosCrudRepository.findAll();
    }

    @Override
    public void delete(Long idVuelo) {
        vuelosCrudRepository.deleteById(idVuelo);
    }

    @Override
    public VuelosEntity update(VuelosEntity vuelo) {
        return vuelosCrudRepository.saveAndFlush(vuelo);
    }

    @Override
    public VuelosEntity save(VuelosEntity vuelo) {
        return vuelosCrudRepository.saveAndFlush(vuelo);
    }

    @Override
    public Optional<VuelosEntity> getByIdVuelo(Long idVuelo) {
        return vuelosCrudRepository.findById(idVuelo);
    }
}
