package com.senasoftproyect.demo.domain.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class PagosEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
            description = "Estado actual del pago",
            example = "PAGADO",
            allowableValues = {"CREDITO", "DEBITO", "PSE", "REEMBOLSADO"},
            required = true
    )
    private MetodoStatus metodo  = MetodoStatus.CREDITO;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Monto total del pago", example = "59.99")
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
            description = "Estado actual del pago",
            example = "PAGADO",
            allowableValues = {"PENDIENTE", "PAGADO", "FALLIDO"},
            required = true
    )
    private PagoStatus estado = PagoStatus.PENDIENTE;

    @CreationTimestamp
    @Column(name = "fecha_pago", updatable = true, nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "nombres_pagador",length = 100, nullable = false)
    private String nombresPagador;

    @Column(name = "tipo_documento", length = 100, nullable = false)
    private String tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefono;

    private Long usuario;

    @Schema(description = "Enumeración que representa los metodos posibles del pago")
    public enum MetodoStatus {
        CREDITO,
        DEBITO,
        PSE
    }

    @Schema(description = "Enumeración que representa los estados posibles del pago")
    public enum PagoStatus {
        PENDIENTE,
        PAGADO,
        CANCELADO
    }

    public PagosEntity(Long idPago, MetodoStatus metodo, BigDecimal total, PagoStatus estado, LocalDateTime fechaPago, String nombresPagador, String tipoDocumento, String numeroDocumento, String email, String telefono, Long usuario) {
        this.idPago = idPago;
        this.metodo = metodo;
        this.total = total;
        this.estado = estado;
        this.fechaPago = fechaPago;
        this.nombresPagador = nombresPagador;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.email = email;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    public PagosEntity() {
    }

    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public MetodoStatus getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoStatus metodo) {
        this.metodo = metodo;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public PagoStatus getEstado() {
        return estado;
    }

    public void setEstado(PagoStatus estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getNombresPagador() {
        return nombresPagador;
    }

    public void setNombresPagador(String nombresPagador) {
        this.nombresPagador = nombresPagador;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
