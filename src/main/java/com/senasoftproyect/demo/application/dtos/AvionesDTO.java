package com.senasoftproyect.demo.application.dtos;


import java.util.List;

public class AvionesDTO {

    private Long idAvion;
    private String modelo;
    private int capacidad;
    private List<AsientosDTO> asientos;

    public AvionesDTO() {
    }

    public AvionesDTO(Long idAvion, String modelo, int capacidad, List<AsientosDTO> asientos) {
        this.idAvion = idAvion;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.asientos = asientos;
    }


    public Long getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(Long idAvion) {
        this.idAvion = idAvion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<AsientosDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientosDTO> asientos) {
        this.asientos = asientos;
    }
}
