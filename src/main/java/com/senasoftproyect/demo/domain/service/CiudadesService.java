package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.CiudadesEntity;

import java.util.List;
import java.util.Optional;

public interface CiudadesService {

    List<CiudadesEntity> getAll();

    Optional<CiudadesEntity> getByIdCiudad(Long idCiudad);

    CiudadesEntity save(CiudadesEntity ciudad);

    CiudadesEntity update(CiudadesEntity ciudad);

    void delete(Long idCiudad);
}
