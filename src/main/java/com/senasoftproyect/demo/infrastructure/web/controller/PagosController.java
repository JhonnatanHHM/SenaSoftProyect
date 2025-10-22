package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.PagosDTO;
import com.senasoftproyect.demo.domain.service.PagosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@Tag(
        name = "Pagos",
        description = "Operaciones CRUD para la gestión de pagos, incluyendo métodos de pago, estado y datos del pagador"
)
public class PagosController {

    @Autowired
    private PagosService pagosService;

    @Operation(
            summary = "Obtener todos los pagos",
            description = "Devuelve una lista completa con todos los pagos registrados en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagosDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<PagosDTO>> getAll() {
        List<PagosDTO> pagos = pagosService.getAll();
        return ResponseEntity.ok(pagos);
    }

    @Operation(
            summary = "Obtener un pago por ID",
            description = "Busca y devuelve la información detallada de un pago específico según su ID.",
            parameters = {
                    @Parameter(name = "idPago", description = "ID del pago a consultar", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pago encontrado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagosDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @GetMapping("/{idPago}")
    public ResponseEntity<PagosDTO> getByIdPago(@PathVariable Long idPago) {
        return pagosService.getByIdPago(idPago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar un nuevo pago",
            description = "Crea un nuevo registro de pago con la información proporcionada en el cuerpo de la solicitud.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pago creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagosDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<PagosDTO> save(@RequestBody PagosDTO pagoDTO) {
        PagosDTO savedPago = pagosService.save(pagoDTO);
        return ResponseEntity.status(201).body(savedPago);
    }

    @Operation(
            summary = "Actualizar un pago existente",
            description = "Permite modificar los datos de un pago previamente registrado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagosDTO.class))),
                    @ApiResponse(responseCode = "400", description = "El ID del pago es nulo o los datos son inválidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Pago no encontrado para actualizar", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @PutMapping
    public ResponseEntity<PagosDTO> update(@RequestBody PagosDTO pagoDTO) {
        try {
            PagosDTO updatedPago = pagosService.update(pagoDTO);
            return ResponseEntity.ok(updatedPago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Eliminar un pago por ID",
            description = "Elimina un registro de pago existente del sistema según su ID.",
            parameters = {
                    @Parameter(name = "idPago", description = "ID del pago a eliminar", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @DeleteMapping("/{idPago}")
    public ResponseEntity<Void> delete(@PathVariable Long idPago) {
        try {
            pagosService.delete(idPago);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
