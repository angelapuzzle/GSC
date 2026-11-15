package com.gsc.gsc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gsc.gsc.dto.ApiResponseDTO;
import com.gsc.gsc.dto.UserCreateRequestDTO;
import com.gsc.gsc.dto.UserPatchRequestDTO;
import com.gsc.gsc.dto.UserResponseDTO;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.service.UsuarioService;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> obtenerUsuario(@PathVariable("id") String serialIdentidad) {
        Usuario usuario = usuarioService.obtenerUsuario(serialIdentidad);
        UserResponseDTO responseDTO = new UserResponseDTO(usuario);
        return ResponseEntity.ok(responseDTO);
    }
    

    @PostMapping
    public ResponseEntity<ApiResponseDTO> crearUsuario(@Valid @RequestBody UserCreateRequestDTO dto) {
        Usuario usuarioCreado = usuarioService.crearUsuario(dto);
        ApiResponseDTO response = new ApiResponseDTO(true, "Usuario creado exitosamente.");

        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(usuarioCreado.getSerialIdentidad()) // O el identificador que uses
                        .toUri();

        return ResponseEntity.created(location).body(response);
    }
    
    @PatchMapping
    public ResponseEntity<UserResponseDTO> actualizarUsuario(@Valid @RequestBody UserPatchRequestDTO dto) {
        Usuario usuario = usuarioService.actualizarUsuario(dto);
        UserResponseDTO response = new UserResponseDTO(usuario);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> eliminarUsuario(@PathVariable("id") String serialIdentidad) {
        usuarioService.eliminarUsuario(serialIdentidad);
        ApiResponseDTO response = new ApiResponseDTO(true, "Usuario eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}
