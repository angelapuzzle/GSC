package com.gsc.gsc.dto;

import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.model.enums.CargoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public interface UserDtos {
    record UserCreateRequestDto(
        @NotBlank (message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario no puede estar vacío.")
        @Size(max = 11, message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario debe no exceder 11 caracteres.")
        String serialIdentidad,

        @NotBlank (message = "El nombre del usuario no puede estar vacío.")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
        String nombre,

        @NotBlank (message = "El apellido del usuario no puede estar vacío.")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
        String apellido,

        @NotNull (message = "El estado del usuario no puede ser nulo.")
        Boolean activo,

        @NotNull (message = "El cargo del usuario no puede ser nulo.")
        CargoUsuario cargo
    ) {}

    record UserPatchRequestDto(
        @NotBlank (message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario no puede estar vacío.")
        @Size(max = 11, message = "El número de cédula de ciudadanía, cédula de extranjería o pasaporte del usuario debe no exceder 11 caracteres.")
        String serialIdentidad,

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
        String nombre,

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
        String apellido,

        Boolean activo,

        CargoUsuario cargo
    ) {}

    record UserResponseDto(
        String serialIdentidad,

        String nombre,

        String apellido,

        Boolean activo,

        CargoUsuario cargo
    ) {

        public static UserResponseDto fromUsuario(Usuario usuario) {

            String serialIdentidad = usuario.getSerialIdentidad();
            String nombre = usuario.getNombre();
            String apellido = usuario.getApellido();
            Boolean activo = usuario.getActivo();
            CargoUsuario cargo = usuario.getCargo();

            return new UserResponseDto(serialIdentidad, nombre, apellido, activo, cargo);
        }
        
    }
}
