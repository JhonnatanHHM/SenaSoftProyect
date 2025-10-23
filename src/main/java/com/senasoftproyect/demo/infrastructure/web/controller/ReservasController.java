package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.ReservasCompleteDTO;
import com.senasoftproyect.demo.application.dtos.ReservasDTO;
import com.senasoftproyect.demo.domain.service.ReservasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Gestión de reservas de vuelos")
public class ReservasController {

    private final ReservasService reservasService;

    @Autowired
    public ReservasController(ReservasService reservasService) {
        this.reservasService = reservasService;
    }

    @Operation(summary = "Obtener todas las reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de reservas obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ReservasCompleteDTO>> getAll() {
        List<ReservasCompleteDTO> reservas = reservasService.getAll();
        return ResponseEntity.ok(reservas);
    }

    @Operation(summary = "Obtener reserva por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservasCompleteDTO> getById(@PathVariable Long idReserva) {
        Optional<ReservasCompleteDTO> reservaOpt = reservasService.getByIdReserva(idReserva);
        return reservaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Crear una nueva reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ReservasDTO> create(@RequestBody ReservasDTO reservaDTO) {
        try {
            ReservasDTO created = reservasService.save(reservaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Actualizar una reserva existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping
    public ResponseEntity<ReservasDTO> update(@RequestBody ReservasDTO reservaDTO) {
        try {
            if (reservaDTO.getIdReserva() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            ReservasDTO updated = reservasService.update(reservaDTO);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Eliminar una reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> delete(@PathVariable Long idReserva) {
        reservasService.delete(idReserva);
        return ResponseEntity.noContent().build();
    }
}
