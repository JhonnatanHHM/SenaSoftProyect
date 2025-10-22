package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.AerolineasCompleteDTO;
import com.senasoftproyect.demo.application.dtos.AerolineasDTO;
import com.senasoftproyect.demo.application.dtos.ImagenesDTO;
import com.senasoftproyect.demo.domain.entitys.AerolineasEntity;
import com.senasoftproyect.demo.domain.entitys.ImagenesEntity;
import com.senasoftproyect.demo.domain.repository.AerolineasRepository;
import com.senasoftproyect.demo.domain.service.AerolineasService;
import com.senasoftproyect.demo.domain.service.StorageService;
import com.senasoftproyect.demo.infrastructure.exceptions.ConflictException;
import com.senasoftproyect.demo.infrastructure.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AerolineasServiceImple implements AerolineasService {

    private final AerolineasRepository aerolineasRepository;
    private final StorageService storageService;

    @Autowired
    public AerolineasServiceImple(AerolineasRepository aerolineasRepository,
                                  StorageService storageService) {
        this.aerolineasRepository = aerolineasRepository;
        this.storageService = storageService;
    }

    @Transactional
    public AerolineasCompleteDTO save(AerolineasDTO dto, MultipartFile file) throws Exception {
        if (aerolineasRepository.existsByNombre(dto.getNombre())) {
            throw new ConflictException("Ya existe una aerolínea con nombre: " + dto.getNombre());
        }

        AerolineasEntity entity = new AerolineasEntity();
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setCelular(dto.getCelular());

        if (file != null && !file.isEmpty()) {
            ImagenesEntity imagen = storageService.uploadImage(file);
            entity.setImagen(imagen);
        }

        AerolineasEntity saved = aerolineasRepository.save(entity);
        return convertToCompleteDto(saved);
    }

    @Override
    public List<AerolineasCompleteDTO> getAll() {
        return aerolineasRepository.getAll()
                .stream()
                .map(this::convertToCompleteDto)
                .collect(Collectors.toList());
    }

    @Override
    public AerolineasCompleteDTO getById(Long id) throws Exception {
        AerolineasEntity entity = aerolineasRepository.getByIdAerolinea(id)
                .orElseThrow(() -> new NotFoundException("Aerolinea no encontrada con id: " + id));
        return convertToCompleteDto(entity);
    }


    @Transactional
    public AerolineasCompleteDTO update(Long id, AerolineasDTO dto, MultipartFile file) throws Exception {
        AerolineasEntity existing = aerolineasRepository.getByIdAerolinea(id)
                .orElseThrow(() -> new NotFoundException("Aerolinea no encontrada con id: " + id));

        existing.setNombre(dto.getNombre());
        existing.setEmail(dto.getEmail());
        existing.setCelular(dto.getCelular());

        if (file != null && !file.isEmpty()) {
            if (existing.getImagen() != null) {
                storageService.updateImage(existing.getImagen().getId(), file);
            } else {
                ImagenesEntity imagen = storageService.uploadImage(file);
                existing.setImagen(imagen);
            }
        }

        AerolineasEntity updated = aerolineasRepository.save(existing);
        return convertToCompleteDto(updated);
    }

    @Transactional
    public void delete(Long id) throws Exception {
        AerolineasEntity existing = aerolineasRepository.getByIdAerolinea(id)
                .orElseThrow(() -> new NotFoundException("Aerolinea no encontrada con id: " + id));

        if (existing.getImagen() != null) {
            storageService.deleteImage(existing.getImagen().getId());
        }

        aerolineasRepository.delete(existing.getIdAerolinea());
    }

    private AerolineasCompleteDTO convertToCompleteDto(AerolineasEntity entity) {
        ImagenesDTO imagenDto = null;
        try {
            if (entity.getImagen() != null) {
                String url = storageService.getPresignedUrl(entity.getImagen().getId());
                imagenDto = new ImagenesDTO(entity.getImagen().getId(), entity.getImagen().getNombreOriginal(), url, entity.getIdAerolinea());
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo URL prefirmada: " + e.getMessage());
        }

        return new AerolineasCompleteDTO(
                entity.getIdAerolinea(),
                entity.getNombre(),
                entity.getEmail(),
                entity.getCelular(),
                imagenDto
        );
    }
}
