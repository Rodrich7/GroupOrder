package com.sportsmatch.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class EncuentroDeportivo {

    private Long id;
    private String deporte;
    private LocalDateTime fechaHora;
    private String cancha;
    private int cupoMinimo;
    private int cupoMaximo;
    private String organizador;
    private EstadoEncuentro estado;

    public EncuentroDeportivo(Long id, String deporte, LocalDateTime fechaHora, String cancha,
                              int cupoMinimo, int cupoMaximo, String organizador) {
        this.id = Objects.requireNonNull(id, "El id del encuentro es obligatorio");
        this.deporte = validarTexto(deporte, "El deporte es obligatorio");
        this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora son obligatorias");
        this.cancha = validarTexto(cancha, "La cancha es obligatoria");
        validarCupos(cupoMinimo, cupoMaximo);
        this.cupoMinimo = cupoMinimo;
        this.cupoMaximo = cupoMaximo;
        this.organizador = validarTexto(organizador, "El organizador es obligatorio");
        this.estado = EstadoEncuentro.BORRADOR;
    }

    private String validarTexto(String valor, String mensajeError) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor.trim();
    }

    private void validarCupos(int cupoMinimo, int cupoMaximo) {
        if (cupoMinimo <= 0 || cupoMaximo <= 0 || cupoMinimo > cupoMaximo) {
            throw new IllegalArgumentException("Los cupos deben ser positivos y el mínimo no puede superar al máximo");
        }
    }

    public Long getId() {
        return id;
    }

    public String getDeporte() {
        return deporte;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getCancha() {
        return cancha;
    }

    public int getCupoMinimo() {
        return cupoMinimo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public EstadoEncuentro getEstado() {
        return estado;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void abrirInscripciones() {
        cambiarEstado(EstadoEncuentro.BORRADOR, EstadoEncuentro.ABIERTO, "abrir inscripciones");
    }

    public void cerrarInscripciones() {
        cambiarEstado(EstadoEncuentro.ABIERTO, EstadoEncuentro.CERRADO, "cerrar inscripciones");
    }

    public void confirmar() {
        cambiarEstado(EstadoEncuentro.CERRADO, EstadoEncuentro.CONFIRMADO, "confirmar");
    }

    public void iniciar() {
        cambiarEstado(EstadoEncuentro.CONFIRMADO, EstadoEncuentro.EN_JUEGO, "iniciar");
    }

    public void finalizar() {
        cambiarEstado(EstadoEncuentro.EN_JUEGO, EstadoEncuentro.FINALIZADO, "finalizar");
    }

    public void cancelar() {
        if (estado == EstadoEncuentro.FINALIZADO || estado == EstadoEncuentro.CANCELADO) {
            throw new IllegalStateException("El encuentro no puede ser cancelado");
        }
        estado = EstadoEncuentro.CANCELADO;
    }

    private void cambiarEstado(EstadoEncuentro estadoEsperado, EstadoEncuentro nuevoEstado, String accion) {
        if (estado != estadoEsperado) {
            throw new IllegalStateException("El encuentro debe estar " + estadoEsperado + " para " + accion);
        }
        estado = nuevoEstado;
    }
}
