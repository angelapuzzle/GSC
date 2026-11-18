package com.gsc.gsc.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.gsc.gsc.security.JwtService;
import com.gsc.gsc.service.AppUserAuthService;
import com.gsc.gsc.dto.ApiResponseDto;
import com.gsc.gsc.dto.AuthDtos.AuthResponseDto;
import com.gsc.gsc.dto.AuthDtos.LoginAppUserDto;
import com.gsc.gsc.dto.AuthDtos.RegisterAppUserDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserAuthService AppUserAuthService;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            AppUserAuthService AppUserAuthService,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.AppUserAuthService = AppUserAuthService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginAppUserDto request) {
        // 1. Autentica credenciales (si fallan, lanza BadCredentialsException -> HTTP 401)
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Extrae el rol del usuario autenticado
        String role = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse("ROLE_USER");

        // 3. Genera el JWT y retorna HTTP 200 OK
        String jwtToken = jwtService.generateToken(authentication.getName(), role);
        return ResponseEntity.ok(new AuthResponseDto(jwtToken, authentication.getName(), role));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')") // Protegido: solo un ADMIN puede crear cuentas
    public ResponseEntity<ApiResponseDto> register(@Valid @RequestBody RegisterAppUserDto request) {
        AppUserAuthService.registrarUsuario(request);
        
        // Retorna HTTP 201 Created sin exponer la entidad ni el hash de la contraseña
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ApiResponseDto(true, "Usuario '" + request.username() + "' creado correctamente"));
    }
}