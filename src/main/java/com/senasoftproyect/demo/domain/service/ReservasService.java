package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.ReservasCompleteDTO;
import com.senasoftproyect.demo.application.dtos.ReservasDTO;

import java.util.List;
import java.util.Optional;

public interface ReservasService {

    List<ReservasCompleteDTO> getAll();

    Optional<ReservasCompleteDTO> getByIdReserva(Long idReserva);

    ReservasDTO save(ReservasDTO reserva);

    ReservasDTO update(ReservasDTO reserva);

    void delete(Long idReserva);
}
