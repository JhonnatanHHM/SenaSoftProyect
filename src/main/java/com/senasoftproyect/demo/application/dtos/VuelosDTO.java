package com.senasoftproyect.demo.application.dtos;

import java.time.LocalDateTime;

public class VuelosDTO {

    private Long idVuelo;
    private Long idCiudadSalida;
    private Long idCiudadLlegada;
    private LocalDateTime horaSalida;
    private LocalDateTime horaLlegada;
    private String lugarSalida;
    private String lugarLlegada;
    private Long idAerolinea;
    private Long idAvion;

    public VuelosDTO() {}

    public VuelosDTO(Long idVuelo, Long idCiudadSalida, Long idCiudadLlegada, LocalDateTime horaSalida, LocalDateTime horaLlegada, String lugarSalida, String lugarLlegada, Long idAerolinea, Long idAvion) {
        this.idVuelo = idVuelo;
        this.idCiudadSalida = idCiudadSalida;
        this.idCiudadLlegada = idCiudadLlegada;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.lugarSalida = lugarSalida;
        this.lugarLlegada = lugarLlegada;
        this.idAerolinea = idAerolinea;
        this.idAvion = idAvion;
    }

    public Long getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Long idVuelo) {
        this.idVuelo = idVuelo;
    }

    public Long getIdCiudadSalida() {
        return idCiudadSalida;
    }

    public void setIdCiudadSalida(Long idCiudadSalida) {
        this.idCiudadSalida = idCiudadSalida;
    }

    public Long getIdCiudadLlegada() {
        return idCiudadLlegada;
    }

    public void setIdCiudadLlegada(Long idCiudadLlegada) {
        this.idCiudadLlegada = idCiudadLlegada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalDateTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalDateTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getLugarSalida() {
        return lugarSalida;
    }

    public void setLugarSalida(String lugarSalida) {
        this.lugarSalida = lugarSalida;
    }

    public String getLugarLlegada() {
        return lugarLlegada;
    }

    public void setLugarLlegada(String lugarLlegada) {
        this.lugarLlegada = lugarLlegada;
    }

    public Long getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(Long idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    public Long getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(Long idAvion) {
        this.idAvion = idAvion;
    }
}
