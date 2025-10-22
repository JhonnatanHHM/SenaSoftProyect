package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.RegionesEntity;

import java.util.List;
import java.util.Optional;

public interface RegionesService {

    List<RegionesEntity> getAll();

    Optional<RegionesEntity> getByIdRegion(Long idRegion);

    RegionesEntity save(RegionesEntity region);

    RegionesEntity update(RegionesEntity region);

    void delete(Long idRegion);
}
