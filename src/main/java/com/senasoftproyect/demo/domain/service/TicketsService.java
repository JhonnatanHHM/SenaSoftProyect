package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.TicketsEntity;

import java.util.List;
import java.util.Optional;

public interface TicketsService {

    List<TicketsEntity> getAll();

    Optional<TicketsEntity> getByIdTicket(Long idTicket);

    TicketsEntity save(TicketsEntity ticket);

    TicketsEntity update(TicketsEntity ticket);

    void delete(Long idTicket);
}
