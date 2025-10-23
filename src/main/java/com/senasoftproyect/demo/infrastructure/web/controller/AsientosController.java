package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.AsientosDTO;
import com.senasoftproyect.demo.domain.service.AsientosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asientos")
@Tag(name = "Asientos", description = "Operaciones CRUD para la gestión de asientos")
public class AsientosController {

    private final AsientosService asientosService;

    @Autowired
    public AsientosController(AsientosService asientosService) {
        this.asientosService = asientosService;
    }

    @Operation(summary = "Obtener todos los asientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de asientos obtenida correctamente")
    })
    @GetMapping
    public List<AsientosDTO> getAll() {
        return asientosService.getAll();
    }

    @Operation(summary = "Obtener un asiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Asiento no encontrado")
    })
    @GetMapping("/{id}")
    public Optional<AsientosDTO> getById(@PathVariable Long id) {
        return asientosService.getByIdAsiento(id);
    }

    @Operation(summary = "Crear un nuevo asiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asiento creado correctamente")
    })
    @PostMapping
    public AsientosDTO create(@RequestBody AsientosDTO dto) {
        return asientosService.save(dto);
    }

    @Operation(summary = "Actualizar un asiento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asiento actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Asiento no encontrado")
    })
    @PutMapping("/{id}")
    public AsientosDTO update(@PathVariable Long id, @RequestBody AsientosDTO dto) {
        dto.setIdAsiento(id);
        return asientosService.update(dto);
    }

    @Operation(summary = "Eliminar un asiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asiento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Asiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        asientosService.delete(id);
    }

    @Operation(
            summary = "Actualizar solo el estado de un asiento",
            description = "Actualiza únicamente el estado (ocupado/libre) de un asiento específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AsientosDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Asiento no encontrado",
                    content = @Content
            )
    })
    @PatchMapping("/estado/{idAsiento}/{idUsuario}")
    public AsientosDTO actualizarEstado(
            @PathVariable Long idAsiento, @PathVariable Long idUsuario) {
        return asientosService.actualizarEstado(idAsiento, idAsiento);
    }

}
