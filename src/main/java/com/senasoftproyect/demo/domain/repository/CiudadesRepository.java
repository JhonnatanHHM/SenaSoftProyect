package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.CiudadesEntity;

import java.util.List;
import java.util.Optional;

public interface CiudadesRepository {

    List<CiudadesEntity> getAll();

    void delete(Long idCiudad);

    CiudadesEntity update(CiudadesEntity ciudad);

    CiudadesEntity save(CiudadesEntity ciudad);

    Optional<CiudadesEntity> getByIdCiudad(Long idCiudad);
}
