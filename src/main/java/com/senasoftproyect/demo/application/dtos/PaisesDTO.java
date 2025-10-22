package com.senasoftproyect.demo.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representación de un país dentro del sistema")
public class PaisesDTO {

    @Schema(description = "Identificador único del país", example = "1")
    private Long idPais;

    @Schema(description = "Nombre del país", example = "Colombia")
    private String nombre;

    public PaisesDTO() {
    }

    public PaisesDTO(Long idPais, String nombre) {
        this.idPais = idPais;
        this.nombre = nombre;
    }

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
