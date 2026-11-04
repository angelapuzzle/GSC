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
    @Column(name="id_reserva")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "serial_identidad", nullable = false)
    private Usuario usuario;

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
}
