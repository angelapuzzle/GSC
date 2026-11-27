package com.gsc.gsc.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component("masker")
public class DataMasker {

    public enum Type {
        EMAIL,
        DOCUMENT,
        PHONE,
        FULL
    }

    public String mask(String value, Type type) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return switch (type) {
            case EMAIL -> maskEmail(value);
            case DOCUMENT, PHONE -> maskLastFour(value);
            case FULL -> "********";
        };
    }

    /**
     * Deja el dato visible si el usuario tiene AL MENOS UNO de los roles indicados.
     * Si no tiene ninguno (o no está autenticado), lo enmascara.
     */
    public String maskUnlessAnyRole(String value, Type type, String... bypassRoles) {
        if (hasAnyRole(bypassRoles)) {
            return value;
        }
        return mask(value, type);
    }

    // Atajos cómodos para Thymeleaf (puedes pasar 1 o varios roles)
    public String email(String value, String... bypassRoles) {
        return maskUnlessAnyRole(value, Type.EMAIL, bypassRoles);
    }

    public String document(String value, String... bypassRoles) {
        return maskUnlessAnyRole(value, Type.DOCUMENT, bypassRoles);
    }

    public String phone(String value, String... bypassRoles) {
        return maskUnlessAnyRole(value, Type.PHONE, bypassRoles);
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return "***" + (atIndex >= 0 ? email.substring(atIndex) : "");
        }
        return email.substring(0, 2) + "*".repeat(atIndex - 2) + email.substring(atIndex);
    }

    private String maskLastFour(String text) {
        if (text.length() <= 4) {
            return "*".repeat(text.length());
        }
        return "*".repeat(text.length() - 4) + text.substring(text.length() - 4);
    }

    private boolean hasAnyRole(String... roles) {
        if (roles == null || roles.length == 0) {
            return false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        Set<String> userAuthorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Arrays.stream(roles).anyMatch(userAuthorities::contains);
    }
}