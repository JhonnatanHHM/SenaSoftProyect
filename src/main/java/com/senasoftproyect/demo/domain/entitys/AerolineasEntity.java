package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "aerolineas")
public class AerolineasEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_aerolinea")
    private Long idAerolinea;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String celular;

    @OneToOne
    @JoinColumn(name = "imagen", nullable = false)
    private ImagenesEntity imagen;

    public AerolineasEntity(Long idAerolinea, String nombre, String email, String celular, ImagenesEntity imagen) {
        this.idAerolinea = idAerolinea;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.imagen = imagen;
    }

    public AerolineasEntity() {
    }

    public Long getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(Long idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public ImagenesEntity getImagen() {
        return imagen;
    }

    public void setImagen(ImagenesEntity imagen) {
        this.imagen = imagen;
    }
}
