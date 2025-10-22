package com.senasoftproyect.demo.application.dtos;

public class TicketsDTO {

    private Long idTicket;
    private Long idReserva;
    private String qrCode;
    private Long idUsuario;

    public TicketsDTO() {
    }

    public TicketsDTO(Long idTicket, Long idReserva, String qrCode, Long idUsuario) {
        this.idTicket = idTicket;
        this.idReserva = idReserva;
        this.qrCode = qrCode;
        this.idUsuario = idUsuario;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
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
