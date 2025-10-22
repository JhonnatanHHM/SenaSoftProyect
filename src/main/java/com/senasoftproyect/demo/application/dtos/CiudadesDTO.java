package com.senasoftproyect.demo.application.dtos;

public class CiudadesDTO {

    private Long idCiudad;
    private String nombre;
    private Long idRegion;
    private String nombreRegion;

    public CiudadesDTO() {
    }

    public CiudadesDTO(Long idCiudad, String nombre, Long idRegion, String nombreRegion) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.idRegion = idRegion;
        this.nombreRegion = nombreRegion;
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
}
