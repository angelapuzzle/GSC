package com.gsc.gsc.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserValidationDTO {

    @NotBlank(message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario no puede estar vacío.")
    @Size(max = 11, message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario debe no exceder 11 caracteres.")
    private String serialIdentidad;

    public String getSerialIdentidad() {
        return serialIdentidad;
    }

    public void setSerialIdentidad(String serialIdentidad) {
        this.serialIdentidad = serialIdentidad;
    }

}
