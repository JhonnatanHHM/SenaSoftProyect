package com.senasoftproyect.demo.application.dtos;

import java.time.LocalDateTime;


public class UsuariosDTO {

    private Long idUsuario;
    private LocalDateTime fechaRegistro;
    private boolean estado;
    private String nombres;
    private String primerApellido;
    private String segundoApellido;
    private String celular;
    private String email;
    private String password;

    public UsuariosDTO() {
    }

    public UsuariosDTO(Long idUsuario, LocalDateTime fechaRegistro, boolean estado, String nombres, String primerApellido, String segundoApellido, String celular, String email, String password) {
        this.idUsuario = idUsuario;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.nombres = nombres;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.celular = celular;
        this.email = email;
        this.password = password;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
