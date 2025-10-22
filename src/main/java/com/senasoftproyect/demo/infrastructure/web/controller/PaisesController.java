package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.PaisesDTO;
import com.senasoftproyect.demo.application.service.PaisesServiceImple;
import com.senasoftproyect.demo.domain.entitys.PaisesEntity;
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
@RequestMapping("/api/paises")
@Tag(name = "Paises", description = "Gestión de países en el sistema")
public class PaisesController {

    private final PaisesServiceImple paisesService;

    @Autowired
    public PaisesController(PaisesServiceImple paisesService) {
        this.paisesService = paisesService;
    }

    @Operation(summary = "Obtener todos los países", description = "Retorna una lista de todos los países registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaisesDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PaisesDTO>> getAll() {
        return ResponseEntity.ok(paisesService.getAllAsDto());
    }

    @Operation(summary = "Obtener país por ID", description = "Busca un país específico por su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaisesDTO.class))),
            @ApiResponse(responseCode = "404", description = "País no encontrado", content = @Content)
    })
    @GetMapping("/{idPais}")
    public ResponseEntity<PaisesDTO> getById(@PathVariable Long idPais) {
        return paisesService.getByIdPais(idPais)
                .map(p -> ResponseEntity.ok(paisesService.convertToDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un nuevo país", description = "Crea un nuevo país en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaisesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PaisesDTO> create(@RequestBody PaisesDTO paisDto) {
        PaisesEntity pais = paisesService.convertToEntity(paisDto);
        PaisesEntity saved = paisesService.save(pais);
        return ResponseEntity.ok(paisesService.convertToDto(saved));
    }

    @Operation(summary = "Actualizar un país", description = "Actualiza los datos de un país existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaisesDTO.class))),
            @ApiResponse(responseCode = "404", description = "País no encontrado", content = @Content)
    })
    @PutMapping("/{idPais}")
    public ResponseEntity<PaisesDTO> update(@PathVariable Long idPais, @RequestBody PaisesDTO paisDto) {
        paisDto.setIdPais(idPais);
        PaisesEntity pais = paisesService.convertToEntity(paisDto);
        PaisesEntity updated = paisesService.update(pais);
        return ResponseEntity.ok(paisesService.convertToDto(updated));
    }

    @Operation(summary = "Eliminar un país", description = "Elimina un país existente según su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "País eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "País no encontrado", content = @Content)
    })
    @DeleteMapping("/{idPais}")
    public ResponseEntity<Void> delete(@PathVariable Long idPais) {
        paisesService.delete(idPais);
        return ResponseEntity.noContent().build();
    }
}
