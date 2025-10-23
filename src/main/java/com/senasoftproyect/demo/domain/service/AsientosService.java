package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.AsientosDTO;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface AsientosService {

    List<AsientosDTO> getAll();

    Optional<AsientosDTO> getByIdAsiento(Long idAsiento);

    AsientosDTO save(AsientosDTO asiento);

    AsientosDTO update(AsientosDTO asiento);

    void delete(Long idAsiento);

    @Transactional
    AsientosDTO actualizarEstado(Long idAsiento, Long idUsuario);
}
