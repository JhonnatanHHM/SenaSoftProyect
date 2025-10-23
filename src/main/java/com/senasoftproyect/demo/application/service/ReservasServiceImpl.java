package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.*;
import com.senasoftproyect.demo.domain.entitys.PagosEntity;
import com.senasoftproyect.demo.domain.entitys.PasajerosEntity;
import com.senasoftproyect.demo.domain.entitys.ReservasEntity;
import com.senasoftproyect.demo.domain.entitys.VuelosEntity;
import com.senasoftproyect.demo.domain.repository.PagosRepository;
import com.senasoftproyect.demo.domain.repository.PasajerosRepository;
import com.senasoftproyect.demo.domain.repository.ReservasRepository;
import com.senasoftproyect.demo.domain.repository.VuelosRepository;
import com.senasoftproyect.demo.domain.service.ReservasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservasServiceImpl implements ReservasService {

    private final ReservasRepository reservasRepository;
    private final PasajerosRepository pasajerosRepository;
    private final PagosRepository pagosRepository;
    private final VuelosRepository vuelosRepository;

    @Autowired
    public ReservasServiceImpl(ReservasRepository reservasRepository,
                               PasajerosRepository pasajerosRepository,
                               PagosRepository pagosRepository,
                               VuelosRepository vuelosRepository) {
        this.reservasRepository = reservasRepository;
        this.pasajerosRepository = pasajerosRepository;
        this.pagosRepository = pagosRepository;
        this.vuelosRepository = vuelosRepository;
    }

    @Override
    public List<ReservasCompleteDTO> getAll() {
        return reservasRepository.getAll().stream()
                .map(this::mapToCompleteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservasCompleteDTO> getByIdReserva(Long idReserva) {
        return reservasRepository.getByIdReserva(idReserva)
                .map(this::mapToCompleteDTO);
    }

    @Override
    public ReservasDTO save(ReservasDTO reserva) {
        ReservasEntity entity = mapToEntity(reserva);
        ReservasEntity saved = reservasRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public ReservasDTO update(ReservasDTO reserva) {
        if (reserva.getIdReserva() == null) {
            throw new IllegalArgumentException("El ID de la reserva no puede ser null para actualizar");
        }
        ReservasEntity entity = mapToEntity(reserva);
        ReservasEntity updated = reservasRepository.save(entity);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long idReserva) {
        reservasRepository.delete(idReserva);
    }

    private ReservasDTO mapToDTO(ReservasEntity entity) {
        List<Long> pasajerosIds = entity.getPasajeros() != null ?
                entity.getPasajeros().stream().map(PasajerosEntity::getId_pasajero).toList() : null;

        Long pagoId = entity.getPago() != null ? entity.getPago().getIdPago() : null;
        Long vueloId = entity.getVuelo() != null ? entity.getVuelo().getIdVuelo() : null;

        return new ReservasDTO(
                entity.getIdReserva(),
                entity.getNumeroReserva(),
                pasajerosIds,
                pagoId,
                vueloId,
                entity.getEstado()
        );
    }

    private ReservasCompleteDTO mapToCompleteDTO(ReservasEntity entity) {
        List<PasajerosDTO> pasajeros = entity.getPasajeros() != null ?
                entity.getPasajeros().stream()
                        .map(p -> new PasajerosDTO(
                                p.getId_pasajero(),
                                p.getNombres(),
                                p.getPrimerApellido(),
                                p.getSegundoApellido(),
                                p.getFechaNacimiento(),
                                p.getGenero(),
                                p.getNumeroDocumento(),
                                p.isInfante(),
                                p.getCelular(),
                                p.getEmail(),
                                p.getAsiento() != null ? p.getAsiento().getIdAsiento() : null
                        )).toList() : null;

        PagosDTO pagoDTO = entity.getPago() != null ?
                new PagosDTO(entity.getPago().getIdPago(), entity.getPago().getTotal(), entity.getPago().getMetodo()) : null;

        VuelosDTO vueloDTO = entity.getVuelo() != null ?
                new VuelosDTO(entity.getVuelo().getIdVuelo(), entity.getVuelo().getIdVuelo(), entity.getVuelo().getOrigen(), entity.getVuelo().getDestino()) : null;

        return new ReservasCompleteDTO(
                entity.getIdReserva(),
                entity.getNumeroReserva(),
                pasajeros,
                pagoDTO,
                vueloDTO,
                entity.getEstado().name()
        );
    }

    private ReservasEntity mapToEntity(ReservasDTO dto) {
        List<PasajerosEntity> pasajeros = dto.getPasajerosIds() != null ?
                dto.getPasajerosIds().stream()
                        .map(id -> pasajerosRepository.getByIdPasajero(id).orElse(null))
                        .toList() : null;

        PagosEntity pago = dto.getPagoId() != null ? pagosRepository.getByIdPago(dto.getPagoId()).orElse(null) : null;
        VuelosEntity vuelo = dto.getVueloId() != null ? vuelosRepository.getByIdVuelo(dto.getVueloId()).orElse(null) : null;

        ReservasEntity.ReservaEstado estado = dto.getEstado() != null ?
                ReservasEntity.ReservaEstado.valueOf(dto.getEstado()) : ReservasEntity.ReservaEstado.PENDIENTE;

        return new ReservasEntity(
                dto.getIdReserva(),
                dto.getNumeroReserva(),
                pasajeros,
                pago,
                vuelo,
                estado
        );
    }
}
