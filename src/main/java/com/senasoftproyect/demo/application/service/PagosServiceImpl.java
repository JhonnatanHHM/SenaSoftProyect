package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.PagosDTO;
import com.senasoftproyect.demo.domain.entitys.*;
import com.senasoftproyect.demo.domain.repository.*;
import com.senasoftproyect.demo.domain.service.PagosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagosServiceImpl implements PagosService {

    private final PagosRepository pagosRepository;
    private final UsuariosRepository usuariosRepository;
    private final ReservasRepository reservasRepository;
    private final TicketsRepository ticketsRepository;

    @Autowired
    public PagosServiceImpl(
            PagosRepository pagosRepository,
            UsuariosRepository usuariosRepository,
            ReservasRepository reservasRepository,
            TicketsRepository ticketsRepository
    ) {
        this.pagosRepository = pagosRepository;
        this.usuariosRepository = usuariosRepository;
        this.reservasRepository = reservasRepository;
        this.ticketsRepository = ticketsRepository;
    }

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

    @Transactional
    @Override
    public PagosDTO save(PagosDTO pagoDTO) {
        PagosEntity pagoEntity = convertToEntity(pagoDTO);


        if (pagoDTO.getUsuario() != null) {
            usuariosRepository.getByIdUsuario(pagoDTO.getUsuario())
                    .ifPresent(usuario -> pagoEntity.setUsuario(usuario.getIdUsuario()));
        }

        PagosEntity saved = pagosRepository.save(pagoEntity);

        if (saved.getEstado() == PagosEntity.PagoStatus.PAGADO) {
            procesarReservaPorPagoAprobado(saved.getIdPago());
        }

        return convertToDTO(saved);
    }

    @Transactional
    @Override
    public PagosDTO update(PagosDTO pagoDTO) {
        if (pagoDTO.getIdPago() == null) {
            throw new IllegalArgumentException("El ID del pago no puede ser nulo para actualizar.");
        }

        PagosEntity pagoEntity = pagosRepository.getByIdPago(pagoDTO.getIdPago())
                .orElseThrow(() -> new RuntimeException("No se encontró el pago con ID: " + pagoDTO.getIdPago()));

        pagoEntity.setMetodo(convertToMetodoStatus(String.valueOf(pagoDTO.getMetodo())));
        pagoEntity.setTotal(pagoDTO.getTotal());
        pagoEntity.setEstado(convertToPagoStatus(String.valueOf(pagoDTO.getEstado())));
        pagoEntity.setFechaPago(pagoDTO.getFechaPago());
        pagoEntity.setNombresPagador(pagoDTO.getNombresPagador());
        pagoEntity.setTipoDocumento(pagoDTO.getTipoDocumento());
        pagoEntity.setNumeroDocumento(pagoDTO.getNumeroDocumento());
        pagoEntity.setEmail(pagoDTO.getEmail());
        pagoEntity.setTelefono(pagoDTO.getTelefono());

        if (pagoDTO.getUsuario() != null) {
            usuariosRepository.getByIdUsuario(pagoDTO.getUsuario())
                    .ifPresent(usuario -> pagoEntity.setUsuario(usuario.getIdUsuario()));
        }

        PagosEntity updated = pagosRepository.save(pagoEntity);


        if (updated.getEstado() == PagosEntity.PagoStatus.PAGADO) {
            procesarReservaPorPagoAprobado(updated.getIdPago());
        }

        return convertToDTO(updated);
    }

    @Override
    public void delete(Long idPago) {
        if (!pagosRepository.existsById(idPago)) {
            throw new RuntimeException("No se encontró el pago con ID: " + idPago);
        }
        pagosRepository.delete(idPago);
    }


    private void procesarReservaPorPagoAprobado(Long pago) {
        reservasRepository.findByPago_IdPago(pago).ifPresent(reserva -> {

            reserva.setEstado(ReservasEntity.ReservaEstado.CONFIRMADO);
            reservasRepository.save(reserva);


            TicketsEntity ticket = new TicketsEntity();
            ticket.setReserva(reserva);
            ticket.setUsuario(reserva.getUsuario());

            ticketsRepository.save(ticket);
        });
    }


    private PagosDTO convertToDTO(PagosEntity entity) {
        Long usuarioId = entity.getUsuario() != null ? entity.getUsuario() : null;

        return new PagosDTO(
                entity.getIdPago(),
                entity.getMetodo(),
                entity.getTotal(),
                entity.getEstado(),
                entity.getFechaPago(),
                entity.getNombresPagador(),
                entity.getTipoDocumento(),
                entity.getNumeroDocumento(),
                entity.getEmail(),
                entity.getTelefono(),
                usuarioId
        );
    }


    private PagosEntity convertToEntity(PagosDTO dto) {
        PagosEntity entity = new PagosEntity();

        entity.setIdPago(dto.getIdPago());
        entity.setMetodo(convertToMetodoStatus(String.valueOf(dto.getMetodo())));
        entity.setTotal(dto.getTotal());
        entity.setEstado(convertToPagoStatus(String.valueOf(dto.getEstado())));
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
            return PagosEntity.MetodoStatus.CREDITO;
        }
    }

    private PagosEntity.PagoStatus convertToPagoStatus(String estado) {
        try {
            return estado != null ? PagosEntity.PagoStatus.valueOf(estado.toUpperCase()) : PagosEntity.PagoStatus.PENDIENTE;
        } catch (IllegalArgumentException e) {
            return PagosEntity.PagoStatus.PENDIENTE;
        }
    }
}
