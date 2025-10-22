package com.senasoftproyect.demo.application.dtos;

public class ImagenesDTO {

    private Long idFile;
    private String nameFile;
    private String keyR2;
    private Long idEntity;

    public ImagenesDTO(Long idFile, String nameFile, String keyR2, Long idEntity) {
        this.idFile = idFile;
        this.nameFile = nameFile;
        this.keyR2 = keyR2;
        this.idEntity = idEntity;
    }

    public ImagenesDTO() {
    }


    public Long getIdFile() {
        return idFile;
    }

    public void setIdFile(Long idFile) {
        this.idFile = idFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getKeyR2() {
        return keyR2;
    }

    public void setKeyR2(String keyR2) {
        this.keyR2 = keyR2;
    }

    public Long getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }
}
