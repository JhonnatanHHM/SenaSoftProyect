package com.senasoftproyect.demo.domain.repository;


import com.senasoftproyect.demo.domain.entitys.AsientosEntity;

import java.util.List;
import java.util.Optional;

public interface AsientosRepository {

    List<AsientosEntity> getAll();

    void delete(Long idAsiento);

    AsientosEntity update(AsientosEntity asiento);

    AsientosEntity save(AsientosEntity asiento);

    Optional<AsientosEntity> getByIdAsiento(Long idAsiento);

}
