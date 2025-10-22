package com.senasoftproyect.demo.application.dtos;

import java.time.LocalDate;

public class PasajerosDTO {

    private Long idPasajero;
    private String nombres;
    private String primerApellido;
    private String segundoApellido;
    private LocalDate fechaNacimiento;
    private String genero;
    private String numeroDocumento;
    private boolean infante;
    private String celular;
    private String email;
    private Long idAsiento; // Referencia al asiento

    public PasajerosDTO() {
    }

    public PasajerosDTO(Long idPasajero, String nombres, String primerApellido, String segundoApellido,
                        LocalDate fechaNacimiento, String genero, String numeroDocumento, boolean infante,
                        String celular, String email, Long idAsiento) {
        this.idPasajero = idPasajero;
        this.nombres = nombres;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.numeroDocumento = numeroDocumento;
        this.infante = infante;
        this.celular = celular;
        this.email = email;
        this.idAsiento = idAsiento;
    }

    public Long getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(Long idPasajero) {
        this.idPasajero = idPasajero;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public boolean isInfante() {
        return infante;
    }

    public void setInfante(boolean infante) {
        this.infante = infante;
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

    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }
}
