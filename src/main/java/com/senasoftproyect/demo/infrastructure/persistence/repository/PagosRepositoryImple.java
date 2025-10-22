package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.PagosEntity;
import com.senasoftproyect.demo.domain.repository.PagosRepository;
import com.senasoftproyect.demo.domain.repository.crud.PagosCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PagosRepositoryImple implements PagosRepository {

    private final PagosCrudRepository pagosCrudRepository;

    @Autowired
    public PagosRepositoryImple(PagosCrudRepository pagosCrudRepository) {
        this.pagosCrudRepository = pagosCrudRepository;
    }

    @Override
    public List<PagosEntity> getAll() {
        return pagosCrudRepository.findAll();
    }

    @Override
    public void delete(Long idPago) {
        pagosCrudRepository.deleteById(idPago);
    }

    @Override
    public PagosEntity update(PagosEntity pago) {
        return pagosCrudRepository.saveAndFlush(pago);
    }

    @Override
    public PagosEntity save(PagosEntity pago) {
        return pagosCrudRepository.saveAndFlush(pago);
    }

    @Override
    public Optional<PagosEntity> getByIdPago(Long idPago) {
        return pagosCrudRepository.findById(idPago);
    }

    @Override
    public boolean existsById(Long idPago) {
        return pagosCrudRepository.existsById(idPago);
    }
}
