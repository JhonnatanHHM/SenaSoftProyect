package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.PaisesEntity;

import java.util.List;
import java.util.Optional;

public interface PaisesService {

    List<PaisesEntity> getAll();

    Optional<PaisesEntity> getByIdPais(Long idPais);

    PaisesEntity save(PaisesEntity pais);

    PaisesEntity update(PaisesEntity pais);

    void delete(Long idPais);
}
