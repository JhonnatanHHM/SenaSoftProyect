package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.RegionesDTO;
import com.senasoftproyect.demo.application.service.RegionesServiceImple;
import com.senasoftproyect.demo.domain.entitys.RegionesEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regiones")
@Tag(name = "Regiones", description = "Operaciones CRUD para la gestión de regiones o departamentos")
public class RegionesController {

    private final RegionesServiceImple regionesService;

    @Autowired
    public RegionesController(RegionesServiceImple regionesService) {
        this.regionesService = regionesService;
    }

    @Operation(
            summary = "Obtener todas las regiones",
            description = "Retorna una lista completa de las regiones registradas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionesDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<RegionesDTO>> getAll() {
        return ResponseEntity.ok(regionesService.getAllAsDto());
    }

    @Operation(
            summary = "Obtener región por ID",
            description = "Busca una región específica según su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Región encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Región no encontrada",
                    content = @Content)
    })
    @GetMapping("/{idRegion}")
    public ResponseEntity<RegionesDTO> getById(@PathVariable Long idRegion) {
        return regionesService.getByIdRegion(idRegion)
                .map(region -> ResponseEntity.ok(regionesService.convertToDto(region)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar nueva región",
            description = "Crea una nueva región asociada a un país existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Región creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<RegionesDTO> create(@RequestBody RegionesDTO regionDto) {
        RegionesEntity region = regionesService.convertToEntity(regionDto);
        RegionesEntity saved = regionesService.save(region);
        return ResponseEntity.ok(regionesService.convertToDto(saved));
    }

    @Operation(
            summary = "Actualizar una región",
            description = "Edita los datos de una región existente según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Región actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Región no encontrada",
                    content = @Content)
    })
    @PutMapping("/{idRegion}")
    public ResponseEntity<RegionesDTO> update(@PathVariable Long idRegion, @RequestBody RegionesDTO regionDto) {
        regionDto.setIdRegion(idRegion);
        RegionesEntity region = regionesService.convertToEntity(regionDto);
        RegionesEntity updated = regionesService.update(region);
        return ResponseEntity.ok(regionesService.convertToDto(updated));
    }

    @Operation(
            summary = "Eliminar una región",
            description = "Elimina una región de la base de datos según su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Región eliminada correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Región no encontrada",
                    content = @Content)
    })
    @DeleteMapping("/{idRegion}")
    public ResponseEntity<Void> delete(@PathVariable Long idRegion) {
        regionesService.delete(idRegion);
        return ResponseEntity.noContent().build();
    }
}
