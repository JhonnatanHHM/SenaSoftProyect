package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes")
public class ImagenesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen", nullable = false)
    private Long idImagen;

    @Column(name = "nombre_original", nullable = false)
    private String nombreOriginal;

    @Column(name = "key_s3", nullable = false, unique = true)
    private String keyS3;

    public ImagenesEntity(Long idImagen, String nombreOriginal, String keyS3) {
        this.idImagen = idImagen;
        this.nombreOriginal = nombreOriginal;
        this.keyS3 = keyS3;
    }

    public ImagenesEntity() {
    }

    public Long getId() {
        return idImagen;
    }

    public void setId(Long idImagen) {
        this.idImagen = idImagen;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public String getKeyS3() {
        return keyS3;
    }

    public void setKeyS3(String keyS3) {
        this.keyS3 = keyS3;
    }
}
