package com.senasoftproyect.demo.application.dtos;

import java.util.List;

public class ReservasCompleteDTO {

    private Long idReserva;
    private String numeroReserva;
    private List<PasajerosDTO> pasajeros;
    private PagosDTO pago;
    private VuelosDTO vuelo;
    private String estado;

    public ReservasCompleteDTO() {
    }

    public ReservasCompleteDTO(Long idReserva, String numeroReserva, List<PasajerosDTO> pasajeros, PagosDTO pago, VuelosDTO vuelo, String estado) {
        this.idReserva = idReserva;
        this.numeroReserva = numeroReserva;
        this.pasajeros = pasajeros;
        this.pago = pago;
        this.vuelo = vuelo;
        this.estado = estado;
    }


    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public String getNumeroReserva() {
        return numeroReserva;
    }

    public void setNumeroReserva(String numeroReserva) {
        this.numeroReserva = numeroReserva;
    }

    public List<PasajerosDTO> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<PasajerosDTO> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public PagosDTO getPago() {
        return pago;
    }

    public void setPago(PagosDTO pago) {
        this.pago = pago;
    }

    public VuelosDTO getVuelo() {
        return vuelo;
    }

    public void setVuelo(VuelosDTO vuelo) {
        this.vuelo = vuelo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
