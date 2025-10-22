package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.PaisesEntity;
import java.util.List;
import java.util.Optional;

public interface PaisesRepository {

    List<PaisesEntity> getAll();

    void delete(Long idPais);

    PaisesEntity update(PaisesEntity pais);

    PaisesEntity save(PaisesEntity pais);

    Optional<PaisesEntity> getByIdPais(Long idPais);
}
