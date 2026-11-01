package com.gsc.gsc.model;

import jakarta.persistence.*;

import com.gsc.gsc.model.enums.DisponibilidadSala;
import java.util.Set;
import java.util.HashSet;

@Entity 
@Table(name = "sala")
public class Sala {

    @Id
    @Column(name = "id_sala")
    private String idSala;

    @Column(nullable = false)
    private DisponibilidadSala disponibilidad;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Computador> computadores = new HashSet<>();

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public DisponibilidadSala getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(DisponibilidadSala disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Set<Computador> getComputadores() {
        return computadores;
    }

    public void addComputador(Computador computador) {
        computadores.add(computador);
        computador.setSala(this);
    }

    public void removeComputador(Computador computador) {
        computadores.remove(computador);
        computador.setSala(null);
    }

}
