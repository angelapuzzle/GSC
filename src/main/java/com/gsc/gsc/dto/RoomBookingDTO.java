package com.gsc.gsc.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class RoomBookingDto {

    private Long idReserva;
    private String idSala;
    private String serialIdentidad;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaFin;

    private String serialesInvitados;
    private String comentarios;

    public RoomBookingDto() {
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public String getSerialIdentidad() {
        return serialIdentidad;
    }

    public void setSerialIdentidad(String serialIdentidad) {
        this.serialIdentidad = serialIdentidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getSerialesInvitados() {
        return serialesInvitados;
    }

    public void setSerialesInvitados(String serialesInvitados) {
        this.serialesInvitados = serialesInvitados;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}