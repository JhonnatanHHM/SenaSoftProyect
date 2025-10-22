package com.senasoftproyect.demo.application.dtos;

public class AerolineasDTO {

    private Long idAerolinea;
    private String nombre;
    private String email;
    private String celular;

    public AerolineasDTO() {
    }

    public AerolineasDTO(Long idAerolinea, String nombre, String email, String celular) {
        this.idAerolinea = idAerolinea;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
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

}
