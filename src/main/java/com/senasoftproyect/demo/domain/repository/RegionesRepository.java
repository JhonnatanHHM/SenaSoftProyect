package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.RegionesEntity;
import java.util.List;
import java.util.Optional;

public interface RegionesRepository {

    List<RegionesEntity> getAll();

    void delete(Long idRegion);

    RegionesEntity update(RegionesEntity region);

    RegionesEntity save(RegionesEntity region);

    Optional<RegionesEntity> getByIdRegion(Long idRegion);
}
