package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.application.dtos.AerolineasCompleteDTO;
import com.senasoftproyect.demo.application.dtos.AerolineasDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AerolineasService {

    List<AerolineasCompleteDTO> getAll();

    AerolineasCompleteDTO save(AerolineasDTO dto, MultipartFile file) throws Exception;

    AerolineasCompleteDTO update(Long id, AerolineasDTO dto, MultipartFile file) throws Exception;

    AerolineasCompleteDTO getById(Long id) throws Exception;

    void delete(Long idAerolinea) throws Exception;
}
