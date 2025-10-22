package com.senasoftproyect.demo.application.dtos;

import java.math.BigDecimal;

public class AsientosDTO {

    private Long idAsiento;
    private String nombre;
    private BigDecimal precio;
    private String estado; // DISPONIBLE, OCUPADO, SELECCIONADO
    private Long idAvion;

    public AsientosDTO() {
    }

    public AsientosDTO(Long idAsiento, String nombre, BigDecimal precio, String estado, Long idAvion) {
        this.idAsiento = idAsiento;
        this.nombre = nombre;
        this.precio = precio;
        this.estado = estado;
        this.idAvion = idAvion;
    }

    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(Long idAvion) {
        this.idAvion = idAvion;
    }
}
