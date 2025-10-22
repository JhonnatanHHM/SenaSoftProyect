package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pasajeros")
public class PasajerosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasajero", nullable = false)
    private Long id_pasajero;

    private String nombres;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String genero;

    @Column(name = "numero_documento", unique = true, nullable = false)
    private String numeroDocumento;

    private boolean infante;

    private String celular;

    private String email;

    @OneToOne
    @JoinColumn(name = "id_asiento")
    private AsientosEntity asiento;

    public PasajerosEntity(Long id_pasajero, String nombres, String primerApellido, String segundoApellido, LocalDate fechaNacimiento, String genero, String numeroDocumento, boolean infante, String celular, String email, AsientosEntity asiento) {
        this.id_pasajero = id_pasajero;
        this.nombres = nombres;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.numeroDocumento = numeroDocumento;
        this.infante = infante;
        this.celular = celular;
        this.email = email;
        this.asiento = asiento;
    }

    public PasajerosEntity() {
    }

    public Long getId_pasajero() {
        return id_pasajero;
    }

    public void setId_pasajero(Long id_pasajero) {
        this.id_pasajero = id_pasajero;
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

    public AsientosEntity getAsiento() {
        return asiento;
    }

    public void setAsiento(AsientosEntity asiento) {
        this.asiento = asiento;
    }
}
