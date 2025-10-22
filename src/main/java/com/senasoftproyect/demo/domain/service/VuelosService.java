package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.VuelosEntity;

import java.util.List;
import java.util.Optional;

public interface VuelosService {

    List<VuelosEntity> getAll();

    Optional<VuelosEntity> getByIdVuelo(Long idVuelo);

    VuelosEntity save(VuelosEntity vuelo);

    VuelosEntity update(VuelosEntity vuelo);

    void delete(Long idVuelo);
}
