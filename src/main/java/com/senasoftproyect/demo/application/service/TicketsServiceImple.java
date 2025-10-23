package com.senasoftproyect.demo.application.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import com.senasoftproyect.demo.application.dtos.*;
import com.senasoftproyect.demo.domain.entitys.*;
import com.senasoftproyect.demo.domain.repository.*;
import com.senasoftproyect.demo.domain.service.ReservasService;
import com.senasoftproyect.demo.domain.service.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketsServiceImple implements TicketsService {

    private final TicketsRepository ticketsRepository;
    private final ReservasRepository reservasRepository;
    private final UsuariosRepository usuariosRepository;
    private final ReservasService reservasService;

    @Autowired
    public TicketsServiceImple(TicketsRepository ticketsRepository,
                               ReservasRepository reservasRepository,
                               UsuariosRepository usuariosRepository,
                               ReservasService reservasService) {
        this.ticketsRepository = ticketsRepository;
        this.reservasRepository = reservasRepository;
        this.usuariosRepository = usuariosRepository;
        this.reservasService = reservasService;
    }

    @Override
    public List<TicketsDTO> getAll() {
        return ticketsRepository.getAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketsDTO> getByIdTicket(Long idTicket) {
        return ticketsRepository.getByIdTicket(idTicket)
                .map(this::convertToDto);
    }

    @Override
    public TicketsDTO save(TicketsDTO ticketDTO) {

        ReservasEntity reserva = reservasRepository.getByIdReserva(ticketDTO.getReserva().getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        UsuariosEntity usuario = usuariosRepository.getByIdUsuario(ticketDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar QR Base64
        String qrData = "Ticket ID: " + ticketDTO.getIdTicket()
                + "\nReserva: " + reserva.getNumeroReserva()
                + "\nUsuario: " + usuario.getIdUsuario();
        String qrBase64 = generarQRCode(qrData, 250, 250);


        TicketsEntity entity = new TicketsEntity();
        entity.setReserva(reserva);
        entity.setUsuario(usuario);
        entity.setQrCode(qrBase64);

        TicketsEntity saved = ticketsRepository.save(entity);
        return convertToDto(saved);
    }

    @Override
    public TicketsDTO update(TicketsDTO dto) {
        if (dto.getIdTicket() == null) {
            throw new IllegalArgumentException("El ID del ticket no puede ser null para actualizar");
        }

        TicketsEntity entity = ticketsRepository.getByIdTicket(dto.getIdTicket())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID " + dto.getIdTicket()));

        ReservasEntity reserva = reservasRepository.getByIdReserva(dto.getReserva().getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        UsuariosEntity usuario = usuariosRepository.getByIdUsuario(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        entity.setReserva(reserva);
        entity.setUsuario(usuario);
        entity.setQrCode(dto.getQrCode());

        TicketsEntity updated = ticketsRepository.save(entity);
        return convertToDto(updated);
    }

    @Override
    public void delete(Long idTicket) {
        ticketsRepository.delete(idTicket);
    }

    @Override
    public List<TicketsDTO> getByUsuarioIdUsuario(Long idUsuario) {
        return ticketsRepository.getByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private TicketsDTO convertToDto(TicketsEntity entity) {
        ReservasCompleteDTO reservaDTO = reservasService.getByIdReserva(entity.getReserva().getIdReserva())
                .orElse(null);

        return new TicketsDTO(
                entity.getIdTicket(),
                reservaDTO,
                entity.getQrCode(),
                entity.getUsuario().getIdUsuario()
        );
    }

    private String generarQRCode(String texto, int ancho, int alto) {
        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(texto, BarcodeFormat.QR_CODE, ancho, alto);

            ByteArrayOutputStream pngOutput = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutput);

            byte[] pngData = pngOutput.toByteArray();
            return Base64.getEncoder().encodeToString(pngData);

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generando el código QR: " + e.getMessage());
        }
    }
}
