package com.senasoftproyect.demo.infrastructure.web.controller;

import com.senasoftproyect.demo.application.dtos.UsuariosDTO;
import com.senasoftproyect.demo.domain.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;

    @Autowired
    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un nuevo usuario en el sistema y devuelve los datos del usuario registrado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuariosDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<UsuariosDTO> register(@RequestBody UsuariosDTO usuarioDto) {
        UsuariosDTO saved = usuariosService.register(usuarioDto);
        return ResponseEntity.ok(saved);
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Devuelve los datos del usuario correspondiente al ID especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuariosDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuariosDTO> getByIdUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long idUsuario) {

        Optional<UsuariosDTO> usuario = usuariosService.getByIdUsuario(idUsuario);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Listar todos los usuarios",
            description = "Devuelve la lista completa de usuarios registrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuariosDTO.class)))
    @GetMapping
    public ResponseEntity<List<UsuariosDTO>> getAll() {
        return ResponseEntity.ok(usuariosService.getAll());
    }

    @Operation(
            summary = "Editar usuario existente",
            description = "Actualiza la información del usuario basado en su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuariosDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PutMapping("/edit")
    public ResponseEntity<UsuariosDTO> editUser(@RequestBody UsuariosDTO usuarioDto) {
        UsuariosDTO updated = usuariosService.editUser(usuarioDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar usuario por ID",
            description = "Elimina un usuario específico del sistema según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long idUsuario) {
        try {
            usuariosService.deleteUser(idUsuario);
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
