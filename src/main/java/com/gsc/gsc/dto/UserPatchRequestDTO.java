package com.gsc.gsc.dto;

import com.gsc.gsc.model.enums.CargoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserPatchRequestDTO{
    
    @NotBlank (message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario no puede estar vacío.")
    @Size(max = 11, message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario debe no exceder 11 caracteres.")
    private String serialIdentidad;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    private Boolean activo;

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
