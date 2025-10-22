package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vuelos")
public class VuelosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vuelo", nullable = false)
    private Long idVuelo;

    @ManyToOne
    @JoinColumn(name = "id_ciudad_salida", nullable = false)
    private CiudadesEntity ciudadSalida;

    @ManyToOne
    @JoinColumn(name = "id_ciudad_llegada", nullable = false)
    private CiudadesEntity ciudadLlegada;

    @Column(name = "hora_salida", nullable = false)
    private LocalDateTime horaSalida;

    @Column(name = "hora_llegada", nullable = false)
    private LocalDateTime horaLlegada;

    @Column(name = "lugar_salida", nullable = false)
    private String lugarSalida;

    @Column(name = "lugar_llegada", nullable = false)
    private String lugarLlegada;

    @ManyToOne
    @JoinColumn(name = "id_aerolinea", nullable = false)
    private AerolineasEntity aerolinea;


    @ManyToOne
    @JoinColumn(name = "id_avion", nullable = false)
    private AvionesEntity avion;

    public VuelosEntity(Long idVuelo, CiudadesEntity ciudadSalida, CiudadesEntity ciudadLlegada, LocalDateTime horaSalida, LocalDateTime horaLlegada, String lugarSalida, String lugarLlegada, AerolineasEntity aerolinea, AvionesEntity avion) {
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

    public VuelosEntity() {
    }

    public Long getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Long idVuelo) {
        this.idVuelo = idVuelo;
    }

    public CiudadesEntity getCiudadSalida() {
        return ciudadSalida;
    }

    public void setCiudadSalida(CiudadesEntity ciudadSalida) {
        this.ciudadSalida = ciudadSalida;
    }

    public CiudadesEntity getCiudadLlegada() {
        return ciudadLlegada;
    }

    public void setCiudadLlegada(CiudadesEntity ciudadLlegada) {
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

    public AerolineasEntity getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(AerolineasEntity aerolinea) {
        this.aerolinea = aerolinea;
    }

    public AvionesEntity getAvion() {
        return avion;
    }

    public void setAvion(AvionesEntity avion) {
        this.avion = avion;
    }
}
