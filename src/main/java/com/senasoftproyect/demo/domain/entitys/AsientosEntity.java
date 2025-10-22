package com.senasoftproyect.demo.domain.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "asientos")
public class AsientosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento", nullable = false)
    private Long idAsiento;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(name = "usuario_reservado")
    private Long usuarioReservado;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Precio del asiento", example = "59.99")
    private BigDecimal precio;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_avion")
    private AvionesEntity avion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
            description = "Estado actual del pago",
            example = "DISPONIBLE",
            allowableValues = {"DISPONIBLE", "OCUPADO", "SELECCIONADO"},
            required = true
    )
    private AsientoStatus estado = AsientoStatus.DISPONIBLE;

    @Schema(description = "Enumeración que representa los estados posibles del pago")
    public enum AsientoStatus {
        DISPONIBLE,
        OCUPADO,
        SELECCIONADO
    }

    public AsientosEntity(Long idAsiento, String nombre, Long usuarioReservado, BigDecimal precio, AvionesEntity avion, AsientoStatus estado) {
        this.idAsiento = idAsiento;
        this.nombre = nombre;
        this.usuarioReservado = usuarioReservado;
        this.precio = precio;
        this.avion = avion;
        this.estado = estado;
    }

    public AsientosEntity() {
    }

    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getUsuarioReservado() {
        return usuarioReservado;
    }

    public void setUsuarioReservado(Long usuarioReservado) {
        this.usuarioReservado = usuarioReservado;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public AsientoStatus getEstado() {
        return estado;
    }

    public void setEstado(AsientoStatus estado) {
        this.estado = estado;
    }

    public AvionesEntity getAvion() {
        return avion;
    }

    public void setAvion(AvionesEntity avion) {
        this.avion = avion;
    }
}
