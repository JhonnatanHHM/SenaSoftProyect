package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.PagosEntity;

import java.util.List;
import java.util.Optional;

public interface PagosService {

    List<PagosEntity> getAll();

    Optional<PagosEntity> getByIdPago(Long idPago);

    PagosEntity save(PagosEntity pago);

    PagosEntity update(PagosEntity pago);

    void delete(Long idPago);
}
