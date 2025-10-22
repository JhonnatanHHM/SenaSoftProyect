package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.PagosEntity;
import java.util.List;
import java.util.Optional;

public interface PagosRepository {

    List<PagosEntity> getAll();

    void delete(Long idPago);

    PagosEntity update(PagosEntity pago);

    PagosEntity save(PagosEntity pago);

    Optional<PagosEntity> getByIdPago(Long idPago);

    boolean existsById(Long idPago);
}
