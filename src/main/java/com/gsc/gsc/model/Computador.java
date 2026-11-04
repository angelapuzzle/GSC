package com.gsc.gsc.model;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.gsc.gsc.model.enums.DisponibilidadComputador;


@Entity 
@Table(name = "computador")
@IdClass(ComputadorId.class)
public class Computador {

    @Id
    @Column(name = "num_computador")
    private Integer numComputador;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    // Todas estas anotaciones son necesarias para que Hibernate pueda mapear
    // correctamente el enum Disponibilidad a un tipo de dato ENUM en PostgreSQL.
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "disponibilidad", nullable = false)
    private DisponibilidadComputador disponibilidad;

    public Integer getNumComputador() {
        return numComputador;
    }

    public void setNumComputador(Integer numComputador) {
        this.numComputador = numComputador;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public DisponibilidadComputador getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(DisponibilidadComputador disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

}