package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.AerolineasEntity;

import java.util.List;
import java.util.Optional;

public interface AerolineasService {

    List<AerolineasEntity> getAll();

    Optional<AerolineasEntity> getByIdAerolinea(Long idAerolinea);

    AerolineasEntity save(AerolineasEntity aerolinea);

    AerolineasEntity update(AerolineasEntity aerolinea);

    void delete(Long idAerolinea);
}
