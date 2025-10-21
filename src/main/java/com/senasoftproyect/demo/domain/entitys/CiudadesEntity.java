package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "ciudades")
public class CiudadesEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long idCiudad;

    @Column(length = 50, nullable = false)
    private String ciudad;

    @Column(length = 50, nullable = false)
    private String region;

    @Column(length = 50, nullable = false)
    private String pais;

    public CiudadesEntity(Long idCiudad, String ciudad, String region, String pais) {
        this.idCiudad = idCiudad;
        this.ciudad = ciudad;
        this.region = region;
        this.pais = pais;
    }

    public CiudadesEntity() {
    }

    public Long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
