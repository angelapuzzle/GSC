package com.gsc.gsc.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "reserva_sala")
public class ReservaSala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    // Relación con Usuario (el profesor o administrativo que reserva)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "serial_identidad", nullable = false)
    private Usuario usuario;

    // Relación con Sala
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "reserva_serial_invitados",
        joinColumns = @JoinColumn(name = "id_reserva")
    )
    @Column(name = "serial_invitado")
    private Set<String> serialesInvitados = new HashSet<>();

    // Getters y Setters
    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }

    public Set<String> getSerialesInvitados() { return serialesInvitados; }
    public void setSerialesInvitados(Set<String> serialesInvitados) { this.serialesInvitados = serialesInvitados; }
}