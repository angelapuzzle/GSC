package com.gsc.gsc.dto;

/**
 * DTO que contiene los datos de autenticación del monitor.
 * Se utiliza para pasar las credenciales desde el controller al servicio.
 */
public class MonitorAuthDto {

    private String username;
    private String password;
    private String documento;

    public MonitorAuthDto() {
    }

    public MonitorAuthDto(String username, String password, String documento) {
        this.username = username;
        this.password = password;
        this.documento = documento;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
