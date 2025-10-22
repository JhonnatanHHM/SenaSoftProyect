package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.VuelosCompleteDTO;
import com.senasoftproyect.demo.application.dtos.VuelosDTO;
import com.senasoftproyect.demo.domain.entitys.VuelosEntity;

import java.util.List;
import java.util.Optional;

public interface VuelosService {

    List<VuelosCompleteDTO> getAll();

    Optional<VuelosCompleteDTO> getByIdVuelo(Long idVuelo);

    VuelosDTO save(VuelosDTO vuelo);

    VuelosDTO update(VuelosDTO vuelo);

    void delete(Long idVuelo);
}
