package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.AvionesEntity;

import java.util.List;
import java.util.Optional;

public interface AvionesRepository {

    List<AvionesEntity> getAll();

    void delete(Long idAvion);

    AvionesEntity update(AvionesEntity avion);

    AvionesEntity save(AvionesEntity avion);

    Optional<AvionesEntity> getByIdAvion(Long idAvion);
}
