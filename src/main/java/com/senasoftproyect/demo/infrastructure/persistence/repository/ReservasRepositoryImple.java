package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.ReservasEntity;
import com.senasoftproyect.demo.domain.repository.ReservasRepository;
import com.senasoftproyect.demo.domain.repository.crud.ReservasCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservasRepositoryImple implements ReservasRepository {

    private final ReservasCrudRepository reservasCrudRepository;

    @Autowired
    public ReservasRepositoryImple(ReservasCrudRepository reservasCrudRepository) {
        this.reservasCrudRepository = reservasCrudRepository;
    }

    @Override
    public List<ReservasEntity> getAll() {
        return reservasCrudRepository.findAll();
    }

    @Override
    public void delete(Long idReserva) {
        reservasCrudRepository.deleteById(idReserva);
    }

    @Override
    public ReservasEntity update(ReservasEntity reserva) {
        return reservasCrudRepository.saveAndFlush(reserva);
    }

    @Override
    public ReservasEntity save(ReservasEntity reserva) {
        return reservasCrudRepository.saveAndFlush(reserva);
    }

    @Override
    public Optional<ReservasEntity> getByIdReserva(Long idReserva) {
        return reservasCrudRepository.findById(idReserva);
    }
}
