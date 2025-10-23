package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.ReservasEntity;

import java.util.List;
import java.util.Optional;

public interface ReservasRepository {

    List<ReservasEntity> getAll();

    void delete(Long idReserva);

    ReservasEntity update(ReservasEntity reserva);

    ReservasEntity save(ReservasEntity reserva);

    Optional<ReservasEntity> getByIdReserva(Long idReserva);

}
