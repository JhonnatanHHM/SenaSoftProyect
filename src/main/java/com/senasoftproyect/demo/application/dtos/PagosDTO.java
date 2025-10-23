package com.senasoftproyect.demo.application.dtos;

import com.senasoftproyect.demo.domain.entitys.PagosEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO para representar los datos de un pago")
public class PagosDTO {

    @Schema(description = "ID único del pago", example = "1")
    private Long idPago;

    @Schema(
            description = "Método utilizado para el pago",
            example = "CREDITO",
            allowableValues = {"CREDITO", "DEBITO", "PSE"}
    )
    private PagosEntity.MetodoStatus metodo;

    @Schema(description = "Monto total del pago", example = "125.50")
    private BigDecimal total;

    @Schema(
            description = "Estado actual del pago",
            example = "PENDIENTE",
            allowableValues = {"PENDIENTE", "PAGADO", "CANCELADO"}
    )
    private PagosEntity.PagoStatus estado;

    @Schema(description = "Fecha y hora en que se realizó el pago", example = "2025-10-22T14:30:00")
    private LocalDateTime fechaPago;

    @Schema(description = "Nombre completo del pagador", example = "Carlos Rodríguez")
    private String nombresPagador;

    @Schema(description = "Tipo de documento del pagador", example = "Cédula de ciudadanía")
    private String tipoDocumento;

    @Schema(description = "Número del documento del pagador", example = "1020456789")
    private String numeroDocumento;

    @Schema(description = "Correo electrónico del pagador", example = "carlos.rodriguez@example.com")
    private String email;

    @Schema(description = "Teléfono de contacto del pagador", example = "+57 3104567890")
    private String telefono;

    @Schema(description = "Id de usuario propietario de la cuenta del pago", example = "1L")
    private Long usuario;

    public PagosDTO() {
    }

    public PagosDTO(Long idPago, PagosEntity.MetodoStatus metodo, BigDecimal total, PagosEntity.PagoStatus estado, LocalDateTime fechaPago, String nombresPagador, String tipoDocumento, String numeroDocumento, String email, String telefono, Long usuario) {
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

    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public PagosEntity.MetodoStatus getMetodo() {
        return metodo;
    }

    public void setMetodo(PagosEntity.MetodoStatus metodo) {
        this.metodo = metodo;
    }

    public PagosEntity.PagoStatus getEstado() {
        return estado;
    }

    public void setEstado(PagosEntity.PagoStatus estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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
