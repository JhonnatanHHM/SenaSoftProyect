package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.PagosDTO;


import java.util.List;
import java.util.Optional;

public interface PagosService {

    List<PagosDTO> getAll();

    Optional<PagosDTO> getByIdPago(Long idPago);

    PagosDTO save(PagosDTO pago);

    PagosDTO update(PagosDTO pago);

    void delete(Long idPago);
}
