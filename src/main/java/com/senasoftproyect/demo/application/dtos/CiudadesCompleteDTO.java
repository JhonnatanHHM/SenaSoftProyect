package com.senasoftproyect.demo.application.dtos;

public class CiudadesCompleteDTO {

    private Long idCiudad;
    private String nombre;
    private Long idRegion;
    private String nombreRegion;
    private String nombrePais;

    public CiudadesCompleteDTO() {
    }

    public CiudadesCompleteDTO(Long idCiudad, String nombre, Long idRegion, String nombreRegion, String nombrePais) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.idRegion = idRegion;
        this.nombreRegion = nombreRegion;
        this.nombrePais = nombrePais;
    }

    public Long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Long idRegion) {
        this.idRegion = idRegion;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
}
