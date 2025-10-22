package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.TicketsEntity;

import java.util.List;
import java.util.Optional;

public interface TicketsRepository {

    List<TicketsEntity> getAll();

    void delete(Long idTicket);

    TicketsEntity update(TicketsEntity ticket);

    TicketsEntity save(TicketsEntity ticket);

    Optional<TicketsEntity> getByIdTicket(Long idTicket);
}
