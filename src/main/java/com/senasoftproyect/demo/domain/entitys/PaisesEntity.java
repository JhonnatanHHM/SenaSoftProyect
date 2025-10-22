package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "paises")
public class PaisesEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_pais")
    private Long idPais;

    @Column(length = 50, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegionesEntity> region;

    public PaisesEntity(Long idPais, String nombre, List<RegionesEntity> region) {
        this.idPais = idPais;
        this.nombre = nombre;
        this.region = region;
    }

    public PaisesEntity() {
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

    public List<RegionesEntity> getRegion() {
        return region;
    }

    public void setRegion(List<RegionesEntity> region) {
        this.region = region;
    }
}
