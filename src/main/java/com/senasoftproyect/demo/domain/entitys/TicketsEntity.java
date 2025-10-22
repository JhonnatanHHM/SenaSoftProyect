package com.senasoftproyect.demo.domain.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class TicketsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private ReservasEntity reserva;

    @Column(name = "qr_code", nullable = false, columnDefinition = "TEXT")
    private String qrCode;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private UsuariosEntity usuario;

    public TicketsEntity(Long idTicket, ReservasEntity reserva, String qrCode, UsuariosEntity usuario) {
        this.idTicket = idTicket;
        this.reserva = reserva;
        this.qrCode = qrCode;
        this.usuario = usuario;
    }

    public TicketsEntity() {
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public ReservasEntity getReserva() {
        return reserva;
    }

    public void setReserva(ReservasEntity reserva) {
        this.reserva = reserva;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public UsuariosEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuariosEntity usuario) {
        this.usuario = usuario;
    }
}
