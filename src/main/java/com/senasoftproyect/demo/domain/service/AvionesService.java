package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.AvionesDTO;
import com.senasoftproyect.demo.domain.entitys.AvionesEntity;

import java.util.List;
import java.util.Optional;

public interface AvionesService {

    List<AvionesDTO> getAll();

    Optional<AvionesDTO> getByIdAvion(Long idAvion);

    AvionesDTO save(AvionesDTO avion);

    AvionesDTO update(AvionesDTO avion);

    void delete(Long idAvion);
}
