package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.VuelosEntity;
import java.util.List;
import java.util.Optional;

public interface VuelosRepository {

    List<VuelosEntity> getAll();

    void delete(Long idVuelo);

    VuelosEntity update(VuelosEntity vuelo);

    VuelosEntity save(VuelosEntity vuelo);

    Optional<VuelosEntity> getByIdVuelo(Long idVuelo);
}
