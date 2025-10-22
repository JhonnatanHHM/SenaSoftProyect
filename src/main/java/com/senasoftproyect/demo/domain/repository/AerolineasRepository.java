package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.AerolineasEntity;

import java.util.List;
import java.util.Optional;

public interface AerolineasRepository {

    boolean existsByNombre(String nombre);

    List<AerolineasEntity> getAll();

    void delete(Long idAerolinea);

    AerolineasEntity update(AerolineasEntity aerolinea);

    AerolineasEntity save(AerolineasEntity aerolinea);

    Optional<AerolineasEntity> getByIdAerolinea(Long idAerolinea);
}
