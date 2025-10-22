package com.senasoftproyect.demo.domain.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ciudades")
public class CiudadesEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long idCiudad;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "region")
    private RegionesEntity region;

    public CiudadesEntity(Long idCiudad, String nombre, RegionesEntity region) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.region = region;
    }

    public CiudadesEntity() {
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

    public RegionesEntity getRegion() {
        return region;
    }

    public void setRegion(RegionesEntity region) {
        this.region = region;
    }
}
