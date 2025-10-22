package com.senasoftproyect.demo.application.dtos;

import com.senasoftproyect.demo.domain.entitys.AsientosEntity;

import java.math.BigDecimal;

public class AsientosDTO {

    private Long idAsiento;
    private String nombre;
    private BigDecimal precio;
    private AsientosEntity.AsientoStatus estado; // DISPONIBLE, OCUPADO, SELECCIONADO
    private Long idAvion;

    public AsientosDTO() {
    }

    public AsientosDTO(Long idAsiento, String nombre, BigDecimal precio, AsientosEntity.AsientoStatus estado, Long idAvion) {
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

    public AsientosEntity.AsientoStatus getEstado() {
        return estado;
    }

    public void setEstado(AsientosEntity.AsientoStatus estado) {
        this.estado = estado;
    }

    public Long getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(Long idAvion) {
        this.idAvion = idAvion;
    }
}
