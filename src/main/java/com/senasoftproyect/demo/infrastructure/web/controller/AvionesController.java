package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.AvionesDTO;
import com.senasoftproyect.demo.domain.service.AvionesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aviones")
@Tag(name = "Aviones", description = "Operaciones CRUD para la gestión de aviones")
public class AvionesController {

    private final AvionesService avionesService;

    @Autowired
    public AvionesController(AvionesService avionesService) {
        this.avionesService = avionesService;
    }

    @Operation(summary = "Obtener todos los aviones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de aviones obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<AvionesDTO>> getAll() {
        List<AvionesDTO> aviones = avionesService.getAll();
        return ResponseEntity.ok(aviones);
    }

    @Operation(summary = "Obtener un avión por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avión encontrado"),
            @ApiResponse(responseCode = "404", description = "Avión no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AvionesDTO> getById(@PathVariable Long id) {
        return avionesService.getByIdAvion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo avión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avión creado correctamente")
    })
    @PostMapping
    public ResponseEntity<AvionesDTO> create(@RequestBody AvionesDTO avionDto) {
        AvionesDTO saved = avionesService.save(avionDto);
        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Actualizar un avión existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avión actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Avión no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AvionesDTO> update(@PathVariable Long id, @RequestBody AvionesDTO avionDto) {
        avionDto.setIdAvion(id);
        AvionesDTO updated = avionesService.update(avionDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar un avión por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avión eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Avión no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        avionesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
