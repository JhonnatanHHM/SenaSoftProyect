package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.PasajerosDTO;
import com.senasoftproyect.demo.domain.service.PasajerosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
@Tag(name = "Pasajeros", description = "Gestión de pasajeros en el sistema")
public class PasajerosController {

    @Autowired
    private PasajerosService pasajerosService;

    @Operation(summary = "Obtener todos los pasajeros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pasajeros obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<PasajerosDTO>> getAllPasajeros() {
        List<PasajerosDTO> pasajeros = pasajerosService.getAll();
        return ResponseEntity.ok(pasajeros);
    }

    @Operation(summary = "Obtener pasajero por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pasajero encontrado"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PasajerosDTO> getPasajeroById(@PathVariable Long id) {
        return pasajerosService.getByIdPasajero(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo pasajero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pasajero creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<PasajerosDTO> createPasajero(@RequestBody PasajerosDTO pasajeroDTO) {
        PasajerosDTO nuevoPasajero = pasajerosService.save(pasajeroDTO);
        return ResponseEntity.status(201).body(nuevoPasajero);
    }

    @Operation(summary = "Actualizar un pasajero existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pasajero actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PasajerosDTO> updatePasajero(@PathVariable Long id, @RequestBody PasajerosDTO pasajeroDTO) {
        pasajeroDTO.setIdPasajero(id);
        try {
            PasajerosDTO actualizado = pasajerosService.update(pasajeroDTO);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un pasajero por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pasajero eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasajero(@PathVariable Long id) {
        pasajerosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
