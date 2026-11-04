package com.gsc.gsc.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

import com.gsc.gsc.model.enums.CargoUsuario;

@Entity
@Table(name = "usuario")
public class Usuario {

    /**
     * Número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario.
     * Este campo es único y se utiliza como identificador principal del usuario.
     */
    @Id
    @Column(name = "serial_identidad")
    private String serialIdentidad;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    /**
     * Indica si el usuario está activo o inactivo en la universidad.
     */
    @Column(nullable = false)
    private Boolean activo;

    // Todas estas anotaciones son necesarias para que Hibernate pueda mapear
    // correctamente el enum Cargo a un tipo de dato ENUM en PostgreSQL.
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "cargo", nullable = false)
    private CargoUsuario cargo;

    public String getSerialIdentidad() {
        return serialIdentidad;
    }

    public void setSerialIdentidad(String serialIdentidad) {
        this.serialIdentidad = serialIdentidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public CargoUsuario getCargo() {
        return cargo;
    }

    public void setCargo(CargoUsuario cargo) {
        this.cargo = cargo;
    }
}
