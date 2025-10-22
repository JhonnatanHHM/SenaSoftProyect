package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.PagosDTO;
import com.senasoftproyect.demo.domain.entitys.PagosEntity;
import com.senasoftproyect.demo.domain.repository.PagosRepository;
import com.senasoftproyect.demo.domain.service.PagosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagosServiceImpl implements PagosService {

    @Autowired
    private PagosRepository pagosRepository;

    @Override
    public List<PagosDTO> getAll() {
        return pagosRepository.getAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PagosDTO> getByIdPago(Long idPago) {
        return pagosRepository.getByIdPago(idPago)
                .map(this::convertToDTO);
    }

    @Override
    public PagosDTO save(PagosDTO pagoDTO) {
        PagosEntity pagoEntity = convertToEntity(pagoDTO);
        PagosEntity saved = pagosRepository.save(pagoEntity);
        return convertToDTO(saved);
    }

    @Override
    public PagosDTO update(PagosDTO pagoDTO) {
        if (pagoDTO.getIdPago() == null) {
            throw new IllegalArgumentException("El ID del pago no puede ser nulo para actualizar.");
        }

        Optional<PagosEntity> optionalPago = pagosRepository.getByIdPago(pagoDTO.getIdPago());
        if (optionalPago.isEmpty()) {
            throw new RuntimeException("No se encontró el pago con ID: " + pagoDTO.getIdPago());
        }

        PagosEntity pagoEntity = convertToEntity(pagoDTO);
        PagosEntity updated = pagosRepository.save(pagoEntity);
        return convertToDTO(updated);
    }

    @Override
    public void delete(Long idPago) {
        if (!pagosRepository.existsById(idPago)) {
            throw new RuntimeException("No se encontró el pago con ID: " + idPago);
        }
        pagosRepository.delete(idPago);
    }

    private PagosDTO convertToDTO(PagosEntity entity) {
        return new PagosDTO(
                entity.getIdPago(),
                entity.getMetodo() != null ? entity.getMetodo().name() : null,
                entity.getTotal(),
                entity.getEstado() != null ? entity.getEstado().name() : null,
                entity.getFechaPago(),
                entity.getNombresPagador(),
                entity.getTipoDocumento(),
                entity.getNumeroDocumento(),
                entity.getEmail(),
                entity.getTelefono()
        );
    }

    private PagosEntity convertToEntity(PagosDTO dto) {
        PagosEntity entity = new PagosEntity();

        entity.setIdPago(dto.getIdPago());
        entity.setMetodo(convertToMetodoStatus(dto.getMetodo()));
        entity.setTotal(dto.getTotal());
        entity.setEstado(convertToPagoStatus(dto.getEstado()));
        entity.setFechaPago(dto.getFechaPago());
        entity.setNombresPagador(dto.getNombresPagador());
        entity.setTipoDocumento(dto.getTipoDocumento());
        entity.setNumeroDocumento(dto.getNumeroDocumento());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());

        return entity;
    }

    private PagosEntity.MetodoStatus convertToMetodoStatus(String metodo) {
        try {
            return metodo != null ? PagosEntity.MetodoStatus.valueOf(metodo.toUpperCase()) : PagosEntity.MetodoStatus.CREDITO;
        } catch (IllegalArgumentException e) {
            return PagosEntity.MetodoStatus.CREDITO; // Valor por defecto seguro
        }
    }

    private PagosEntity.PagoStatus convertToPagoStatus(String estado) {
        try {
            return estado != null ? PagosEntity.PagoStatus.valueOf(estado.toUpperCase()) : PagosEntity.PagoStatus.PENDIENTE;
        } catch (IllegalArgumentException e) {
            return PagosEntity.PagoStatus.PENDIENTE; // Valor por defecto seguro
        }
    }
}
