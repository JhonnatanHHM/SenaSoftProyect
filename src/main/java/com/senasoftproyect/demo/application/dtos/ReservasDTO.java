package com.senasoftproyect.demo.application.dtos;

import com.senasoftproyect.demo.domain.entitys.ReservasEntity;

import java.util.List;

public class ReservasDTO {

    private Long idReserva;
    private String numeroReserva;
    private List<Long> pasajerosIds;
    private Long pagoId;
    private Long vueloId;
    private ReservasEntity.ReservaEstado estado;
    private Long usuario;

    public ReservasDTO() {
    }

    public ReservasDTO(Long idReserva, String numeroReserva, List<Long> pasajerosIds, Long pagoId, Long vueloId, ReservasEntity.ReservaEstado estado, Long usuario) {
        this.idReserva = idReserva;
        this.numeroReserva = numeroReserva;
        this.pasajerosIds = pasajerosIds;
        this.pagoId = pagoId;
        this.vueloId = vueloId;
        this.estado = estado;
        this.usuario = usuario;
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

    public List<Long> getPasajerosIds() {
        return pasajerosIds;
    }

    public void setPasajerosIds(List<Long> pasajerosIds) {
        this.pasajerosIds = pasajerosIds;
    }

    public Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(Long pagoId) {
        this.pagoId = pagoId;
    }

    public Long getVueloId() {
        return vueloId;
    }

    public void setVueloId(Long vueloId) {
        this.vueloId = vueloId;
    }

    public ReservasEntity.ReservaEstado getEstado() {
        return estado;
    }

    public void setEstado(ReservasEntity.ReservaEstado estado) {
        this.estado = estado;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
