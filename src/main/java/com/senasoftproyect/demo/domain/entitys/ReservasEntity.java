package com.senasoftproyect.demo.domain.entitys;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "reservas")
public class ReservasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "numero_reserva", unique = true, nullable = false)
    private String numeroReserva;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reserva_id")
    private List<PasajerosEntity> pasajeros;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pago_id")
    private PagosEntity pago;

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private VuelosEntity vuelo;

    @Enumerated(EnumType.STRING)
    private ReservaEstado estado;

    private enum ReservaEstado {
        CONFIRMADO,
        CANCELADO,
        PENDIENTE
    }

    public ReservasEntity(Long idReserva, String numeroReserva, List<PasajerosEntity> pasajeros, PagosEntity pago, VuelosEntity vuelo, ReservaEstado estado) {
        this.idReserva = idReserva;
        this.numeroReserva = numeroReserva;
        this.pasajeros = pasajeros;
        this.pago = pago;
        this.vuelo = vuelo;
        this.estado = estado;
    }

    public ReservasEntity() {
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

    public List<PasajerosEntity> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<PasajerosEntity> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public PagosEntity getPago() {
        return pago;
    }

    public void setPago(PagosEntity pago) {
        this.pago = pago;
    }

    public VuelosEntity getVuelo() {
        return vuelo;
    }

    public void setVuelo(VuelosEntity vuelo) {
        this.vuelo = vuelo;
    }

    public ReservaEstado getEstado() {
        return estado;
    }

    public void setEstado(ReservaEstado estado) {
        this.estado = estado;
    }
}
