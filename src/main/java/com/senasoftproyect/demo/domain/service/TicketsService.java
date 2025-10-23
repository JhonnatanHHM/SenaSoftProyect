package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.TicketsDTO;
import com.senasoftproyect.demo.domain.entitys.TicketsEntity;

import java.util.List;
import java.util.Optional;

public interface TicketsService {

    List<TicketsDTO> getAll();

    Optional<TicketsDTO> getByIdTicket(Long idTicket);

    TicketsDTO save(TicketsDTO ticket);

    TicketsDTO update(TicketsDTO ticket);

    void delete(Long idTicket);

    List<TicketsDTO> getByUsuarioIdUsuario(Long idUsuario);
}
