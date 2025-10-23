package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.TicketsEntity;
import com.senasoftproyect.demo.domain.repository.TicketsRepository;
import com.senasoftproyect.demo.domain.repository.crud.TicketsCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TicketsRepositoryImple implements TicketsRepository {

    private final TicketsCrudRepository ticketsCrudRepository;

    @Autowired
    public TicketsRepositoryImple(TicketsCrudRepository ticketsCrudRepository) {
        this.ticketsCrudRepository = ticketsCrudRepository;
    }

    @Override
    public List<TicketsEntity> getAll() {
        return ticketsCrudRepository.findAll();
    }

    @Override
    public void delete(Long idTicket) {
        ticketsCrudRepository.deleteById(idTicket);
    }

    @Override
    public TicketsEntity update(TicketsEntity ticket) {
        return ticketsCrudRepository.saveAndFlush(ticket);
    }

    @Override
    public TicketsEntity save(TicketsEntity ticket) {
        return ticketsCrudRepository.saveAndFlush(ticket);
    }

    @Override
    public Optional<TicketsEntity> getByIdTicket(Long idTicket) {
        return ticketsCrudRepository.findById(idTicket);
    }

    @Override
    public List<TicketsEntity> getByUsuarioIdUsuario(Long idUsuario) {
        return ticketsCrudRepository.findByUsuarioIdUsuario(idUsuario);
    }
}
