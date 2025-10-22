package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "aviones")
public class AvionesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion", nullable = false)
    private Long idAvion;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int capacidad;

    @OneToMany(mappedBy = "avion", cascade = CascadeType.ALL)
    private List<AsientosEntity> asientos;

    public AvionesEntity(Long idAvion, String modelo, int capacidad, List<AsientosEntity> asientos) {
        this.idAvion = idAvion;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.asientos = asientos;
    }

    public AvionesEntity() {
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

    public List<AsientosEntity> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientosEntity> asientos) {
        this.asientos = asientos;
    }
}
