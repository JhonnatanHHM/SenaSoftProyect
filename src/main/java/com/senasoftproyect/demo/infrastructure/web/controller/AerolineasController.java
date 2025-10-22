package com.senasoftproyect.demo.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senasoftproyect.demo.application.dtos.AerolineasCompleteDTO;
import com.senasoftproyect.demo.application.dtos.AerolineasDTO;
import com.senasoftproyect.demo.domain.service.AerolineasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/aerolineas")
@Tag(name = "Aerolineas", description = "Operaciones CRUD para aerolíneas")
public class AerolineasController {

    private final AerolineasService aerolineasService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AerolineasController(AerolineasService aerolineasService, ObjectMapper objectMapper) {
        this.aerolineasService = aerolineasService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Guardar una aerolínea con imagen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aerolínea creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AerolineasCompleteDTO> saveAerolinea(
            @RequestPart("aerolinea") @Parameter(description = "DTO de aerolínea", required = true,
                    content = @Content(schema = @Schema(implementation = AerolineasDTO.class))) String aerolineaJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            AerolineasDTO dto = objectMapper.readValue(aerolineaJson, AerolineasDTO.class);
            AerolineasCompleteDTO result = aerolineasService.save(dto, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener todas las aerolíneas")
    @GetMapping
    public ResponseEntity<List<AerolineasCompleteDTO>> getAll() {
        List<AerolineasCompleteDTO> list = aerolineasService.getAll();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener aerolínea por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AerolineasCompleteDTO> getById(@PathVariable Long id) {
        try {
            AerolineasCompleteDTO dto = aerolineasService.getById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar aerolínea con imagen")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AerolineasCompleteDTO> updateAerolinea(
            @PathVariable Long id,
            @RequestPart("aerolinea") @Parameter(description = "DTO de aerolínea", required = true,
                    content = @Content(schema = @Schema(implementation = AerolineasDTO.class))) String aerolineaJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            AerolineasDTO dto = objectMapper.readValue(aerolineaJson, AerolineasDTO.class);
            AerolineasCompleteDTO result = aerolineasService.update(id, dto, imagen);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar aerolínea por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAerolinea(@PathVariable Long id) {
        try {
            aerolineasService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
