package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.VuelosCompleteDTO;
import com.senasoftproyect.demo.application.dtos.VuelosDTO;
import com.senasoftproyect.demo.domain.service.VuelosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
@Tag(name = "Vuelos", description = "Operaciones CRUD para la gestión de vuelos")
public class VuelosController {

    private final VuelosService vuelosService;

    @Autowired
    public VuelosController(VuelosService vuelosService) {
        this.vuelosService = vuelosService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los vuelos", description = "Retorna la lista completa de vuelos con sus detalles")
    public ResponseEntity<List<VuelosCompleteDTO>> getAllVuelos() {
        List<VuelosCompleteDTO> vuelos = vuelosService.getAll();
        return new ResponseEntity<>(vuelos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vuelo por ID", description = "Retorna un vuelo específico por su ID")
    public ResponseEntity<VuelosCompleteDTO> getVueloById(
            @Parameter(description = "ID del vuelo", required = true)
            @PathVariable Long id) {
        return vuelosService.getByIdVuelo(id)
                .map(vuelo -> new ResponseEntity<>(vuelo, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo vuelo", description = "Permite crear un vuelo con los datos proporcionados")
    public ResponseEntity<VuelosDTO> createVuelo(
            @Parameter(description = "Datos del vuelo a crear", required = true)
            @RequestBody VuelosDTO vueloDTO) {
        VuelosDTO created = vuelosService.save(vueloDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Actualizar un vuelo", description = "Actualiza los datos de un vuelo existente")
    public ResponseEntity<VuelosDTO> updateVuelo(
            @Parameter(description = "Datos del vuelo a actualizar", required = true)
            @RequestBody VuelosDTO vueloDTO) {
        VuelosDTO updated = vuelosService.update(vueloDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un vuelo", description = "Elimina un vuelo por su ID")
    public ResponseEntity<Void> deleteVuelo(
            @Parameter(description = "ID del vuelo a eliminar", required = true)
            @PathVariable Long id) {
        vuelosService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
