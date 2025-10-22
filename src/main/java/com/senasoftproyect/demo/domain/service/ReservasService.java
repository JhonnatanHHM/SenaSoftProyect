package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.ReservasEntity;

import java.util.List;
import java.util.Optional;

public interface ReservasService {

    List<ReservasEntity> getAll();

    Optional<ReservasEntity> getByIdReserva(Long idReserva);

    ReservasEntity save(ReservasEntity reserva);

    ReservasEntity update(ReservasEntity reserva);

    void delete(Long idReserva);
}
