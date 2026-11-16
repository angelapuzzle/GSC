package com.gsc.gsc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public interface AuthDtos {

    record LoginAppUserDto(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
    ) {}

    record RegisterAppUserDto(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size (min = 8, max=30, message = "La contraseña debe tener al menos 8 caracteres y máximo 30 caracteres")
        String password,
        
        @NotBlank(message = "El rol es obligatorio")
        @Pattern(regexp = "ADMIN|MONITOR|USER", message = "El rol debe ser 'ADMIN', 'MONITOR' o 'USER'")
        String role
    ) {}

    record AuthResponseDto(
        String token,
        String username,
        String role
    ) {}
    
}
