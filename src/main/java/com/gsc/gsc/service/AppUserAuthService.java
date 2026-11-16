package com.gsc.gsc.service;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gsc.gsc.exception.ResourceAlreadyExistsException;
import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.repository.AppUserRepository;

import com.gsc.gsc.dto.AuthDtos.RegisterAppUserDto;

@Service 
public class AppUserAuthService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserAuthService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override 
    public UserDetails loadUserByUsername(String username) {
        AppUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .build();
    }

    public boolean existsUsers() {
        return appUserRepository.count() > 0;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AppUser registrarUsuario(String username, String rawPassword, String role) {
        if (appUserRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("El usuario " + username + " ya existe");
        }

        AppUser nuevoUsuario = new AppUser();
        nuevoUsuario.setUsername(username);
        // Encriptación con BCrypt antes de guardar en BD
        nuevoUsuario.setPassword(passwordEncoder.encode(rawPassword));
        nuevoUsuario.setRole(role);

        return appUserRepository.save(nuevoUsuario);
    }
    public AppUser registrarUsuario(RegisterAppUserDto request) {
        return registrarUsuario(request.username(), request.password(), request.role());
    }
}
