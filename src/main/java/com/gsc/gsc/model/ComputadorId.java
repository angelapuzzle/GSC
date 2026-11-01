package com.gsc.gsc.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa la clave primaria compuesta de la entidad Computador.
 */
@Embeddable 
public class ComputadorId implements Serializable {

    private String sala;
    private Integer numComputador;

    public ComputadorId() {}

    public ComputadorId(String sala, Integer numComputador) {
        this.sala = sala;
        this.numComputador = numComputador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComputadorId)) return false;
        ComputadorId that = (ComputadorId) o;
        return sala.equals(that.sala) && numComputador.equals(that.numComputador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sala, numComputador);
    }
}