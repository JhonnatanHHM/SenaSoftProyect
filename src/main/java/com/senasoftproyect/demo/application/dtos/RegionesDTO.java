package com.senasoftproyect.demo.application.dtos;

public class RegionesDTO {

    private Long idRegion;
    private String nombre;
    private Long idPais;
    private String nombrePais;

    public RegionesDTO() {
    }

    public RegionesDTO(Long idRegion, String nombre, Long idPais, String nombrePais) {
        this.idRegion = idRegion;
        this.nombre = nombre;
        this.idPais = idPais;
        this.nombrePais = nombrePais;
    }

    public Long getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Long idRegion) {
        this.idRegion = idRegion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
}
