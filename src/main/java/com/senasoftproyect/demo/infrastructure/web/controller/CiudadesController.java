package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.CiudadesCompleteDTO;
import com.senasoftproyect.demo.application.dtos.CiudadesDTO;
import com.senasoftproyect.demo.application.service.CiudadesServiceImple;
import com.senasoftproyect.demo.domain.entitys.CiudadesEntity;
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
@RequestMapping("/api/ciudades")
@Tag(name = "Ciudades", description = "Operaciones CRUD para la gestión de ciudades")
public class CiudadesController {

    private final CiudadesServiceImple ciudadesService;

    @Autowired
    public CiudadesController(CiudadesServiceImple ciudadesService) {
        this.ciudadesService = ciudadesService;
    }

    @Operation(
            summary = "Obtener todas las ciudades",
            description = "Retorna una lista de todas las ciudades registradas en la base de datos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ciudades obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CiudadesDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CiudadesCompleteDTO>> getAll() {
        return ResponseEntity.ok(ciudadesService.getAllCompleteDto());
    }

    @Operation(
            summary = "Obtener ciudad por ID",
            description = "Busca una ciudad específica utilizando su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ciudad encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CiudadesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada",
                    content = @Content)
    })
    @GetMapping("/{idCiudad}")
    public ResponseEntity<CiudadesDTO> getById(@PathVariable Long idCiudad) {
        return ciudadesService.getByIdCiudad(idCiudad)
                .map(ciudad -> ResponseEntity.ok(ciudadesService.convertToDto(ciudad)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar nueva ciudad",
            description = "Crea una nueva ciudad y la asocia con una región existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ciudad creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CiudadesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CiudadesDTO> create(@RequestBody CiudadesDTO ciudadDto) {
        CiudadesEntity ciudad = ciudadesService.convertToEntity(ciudadDto);
        CiudadesEntity saved = ciudadesService.save(ciudad);
        return ResponseEntity.ok(ciudadesService.convertToDto(saved));
    }

    @Operation(
            summary = "Actualizar una ciudad",
            description = "Edita los datos de una ciudad existente según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ciudad actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CiudadesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada",
                    content = @Content)
    })
    @PutMapping("/{idCiudad}")
    public ResponseEntity<CiudadesDTO> update(@PathVariable Long idCiudad, @RequestBody CiudadesDTO ciudadDto) {
        ciudadDto.setIdCiudad(idCiudad);
        CiudadesEntity ciudad = ciudadesService.convertToEntity(ciudadDto);
        CiudadesEntity updated = ciudadesService.update(ciudad);
        return ResponseEntity.ok(ciudadesService.convertToDto(updated));
    }

    @Operation(
            summary = "Eliminar una ciudad",
            description = "Elimina una ciudad de la base de datos por su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ciudad eliminada correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada",
                    content = @Content)
    })
    @DeleteMapping("/{idCiudad}")
    public ResponseEntity<Void> delete(@PathVariable Long idCiudad) {
        ciudadesService.delete(idCiudad);
        return ResponseEntity.noContent().build();
    }
}
