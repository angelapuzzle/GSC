package com.gsc.gsc.dto;

import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.model.enums.CargoUsuario;

public class UserResponseDTO {

    private String serialIdentidad;

    private String nombre;

    private String apellido;

    private Boolean activo;

    private CargoUsuario cargo;

    public UserResponseDTO(Usuario usuario) {
        this.serialIdentidad = usuario.getSerialIdentidad();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.activo = usuario.getActivo();
        this.cargo = usuario.getCargo();
    }

    public String getSerialIdentidad() {
        return serialIdentidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Boolean getActivo() {
        return activo;
    }

    public CargoUsuario getCargo() {
        return cargo;
    }

    

}
