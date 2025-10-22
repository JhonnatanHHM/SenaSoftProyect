package com.senasoftproyect.demo.application.dtos;

import com.senasoftproyect.demo.domain.entitys.AsientosEntity;

public class AsientosStatusDTO {

    private Long idAsiento;

    private AsientosEntity.AsientoStatus nuevoEstado;

    private Long usuarioReservado;

    public AsientosStatusDTO(Long idAsiento, AsientosEntity.AsientoStatus nuevoEstado, Long usuarioReservado) {
        this.idAsiento = idAsiento;
        this.nuevoEstado = nuevoEstado;
        this.usuarioReservado = usuarioReservado;
    }

    public AsientosStatusDTO() {
    }

    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public AsientosEntity.AsientoStatus getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(AsientosEntity.AsientoStatus nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public Long getUsuarioReservado() {
        return usuarioReservado;
    }

    public void setUsuarioReservado(Long usuarioReservado) {
        this.usuarioReservado = usuarioReservado;
    }
}
