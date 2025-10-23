package com.senasoftproyect.demo.application.dtos;

public class TicketsDTO {

    private Long idTicket;
    private ReservasCompleteDTO Reserva;
    private String qrCode;
    private Long idUsuario;

    public TicketsDTO() {
    }

    public TicketsDTO(Long idTicket, ReservasCompleteDTO Reserva, String qrCode, Long idUsuario) {
        this.idTicket = idTicket;
        this.Reserva = Reserva;
        this.qrCode = qrCode;
        this.idUsuario = idUsuario;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public ReservasCompleteDTO getReserva() {
        return Reserva;
    }

    public void setReserva(ReservasCompleteDTO reserva) {
        this.Reserva = reserva;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
