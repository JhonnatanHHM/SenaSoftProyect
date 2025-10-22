package com.senasoftproyect.demo.domain.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "regiones")
public class RegionesEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_region")
    private Long idRegion;

    @Column(length = 50, nullable = false)
    private String nombre;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pais")
    private PaisesEntity pais;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CiudadesEntity> ciudades;

    public RegionesEntity(Long idRegion, String nombre, PaisesEntity pais, List<CiudadesEntity> ciudades) {
        this.idRegion = idRegion;
        this.nombre = nombre;
        this.pais = pais;
        this.ciudades = ciudades;
    }

    public RegionesEntity() {
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

    public PaisesEntity getPais() {
        return pais;
    }

    public void setPais(PaisesEntity pais) {
        this.pais = pais;
    }

    public List<CiudadesEntity> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<CiudadesEntity> ciudades) {
        this.ciudades = ciudades;
    }
}
