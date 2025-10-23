package com.senasoftproyect.demo.application.dtos;

import com.senasoftproyect.demo.domain.entitys.ReservasEntity;

import java.util.List;

public class ReservasCompleteDTO {

    private Long idReserva;
    private String numeroReserva;
    private List<PasajerosDTO> pasajeros;
    private PagosDTO pago;
    private VuelosCompleteDTO vuelo;
    private ReservasEntity.ReservaEstado estado;

    public ReservasCompleteDTO() {
    }

    public ReservasCompleteDTO(Long idReserva, String numeroReserva, List<PasajerosDTO> pasajeros, PagosDTO pago, VuelosCompleteDTO vuelo, ReservasEntity.ReservaEstado estado) {
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

    public VuelosCompleteDTO getVuelo() {
        return vuelo;
    }

    public void setVuelo(VuelosCompleteDTO vuelo) {
        this.vuelo = vuelo;
    }

    public ReservasEntity.ReservaEstado getEstado() {
        return estado;
    }

    public void setEstado(ReservasEntity.ReservaEstado estado) {
        this.estado = estado;
    }
}
