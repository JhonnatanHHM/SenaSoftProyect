package com.senasoftproyect.demo.application.dtos;

import java.time.LocalDateTime;

public class VuelosCompleteDTO {

    private Long idVuelo;
    private CiudadesDTO ciudadSalida;
    private CiudadesDTO ciudadLlegada;
    private LocalDateTime horaSalida;
    private LocalDateTime horaLlegada;
    private String lugarSalida;
    private String lugarLlegada;
    private AerolineasCompleteDTO aerolinea;
    private AvionesDTO avion;

    public VuelosCompleteDTO() {}

    public VuelosCompleteDTO(Long idVuelo, CiudadesDTO ciudadSalida, CiudadesDTO ciudadLlegada,
                             LocalDateTime horaSalida, LocalDateTime horaLlegada, String lugarSalida, String lugarLlegada,
                             AerolineasCompleteDTO aerolinea, AvionesDTO avion) {
        this.idVuelo = idVuelo;
        this.ciudadSalida = ciudadSalida;
        this.ciudadLlegada = ciudadLlegada;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.lugarSalida = lugarSalida;
        this.lugarLlegada = lugarLlegada;
        this.aerolinea = aerolinea;
        this.avion = avion;
    }

    public Long getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Long idVuelo) {
        this.idVuelo = idVuelo;
    }

    public CiudadesDTO getCiudadSalida() {
        return ciudadSalida;
    }

    public void setCiudadSalida(CiudadesDTO ciudadSalida) {
        this.ciudadSalida = ciudadSalida;
    }

    public CiudadesDTO getCiudadLlegada() {
        return ciudadLlegada;
    }

    public void setCiudadLlegada(CiudadesDTO ciudadLlegada) {
        this.ciudadLlegada = ciudadLlegada;
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

    public AerolineasCompleteDTO getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(AerolineasCompleteDTO aerolinea) {
        this.aerolinea = aerolinea;
    }

    public AvionesDTO getAvion() {
        return avion;
    }

    public void setAvion(AvionesDTO avion) {
        this.avion = avion;
    }
}
