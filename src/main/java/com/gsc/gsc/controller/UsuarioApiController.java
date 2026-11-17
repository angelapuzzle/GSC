package com.gsc.gsc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gsc.gsc.dto.ApiResponseDto;
import com.gsc.gsc.dto.UserDtos.UserCreateRequestDto;
import com.gsc.gsc.dto.UserDtos.UserPatchRequestDto;
import com.gsc.gsc.dto.UserDtos.UserResponseDto;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.service.UsuarioService;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping ("/api/usuarios")
public class UsuarioApiController {

    private final UsuarioService usuarioService;

    public UsuarioApiController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MONITOR') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> obtenerUsuario(@PathVariable("id") String serialIdentidad) {
        Usuario usuario = usuarioService.obtenerUsuario(serialIdentidad);
        UserResponseDto responseDTO = UserResponseDto.fromUsuario(usuario);
        return ResponseEntity.ok(responseDTO);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('MONITOR')")
    @PostMapping
    public ResponseEntity<ApiResponseDto> crearUsuario(@Valid @RequestBody UserCreateRequestDto dto) {
        Usuario usuarioCreado = usuarioService.crearUsuario(dto);
        ApiResponseDto response = new ApiResponseDto(true, "Usuario creado exitosamente.");

        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(usuarioCreado.getSerialIdentidad()) // O el identificador que uses
                        .toUri();

        return ResponseEntity.created(location).body(response);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('MONITOR')")
    @PatchMapping
    public ResponseEntity<UserResponseDto> actualizarUsuario(@Valid @RequestBody UserPatchRequestDto dto) {
        Usuario usuario = usuarioService.actualizarUsuario(dto);
        UserResponseDto response = UserResponseDto.fromUsuario(usuario);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MONITOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> eliminarUsuario(@PathVariable("id") String serialIdentidad) {
        usuarioService.eliminarUsuario(serialIdentidad);
        ApiResponseDto response = new ApiResponseDto(true, "Usuario eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}
