package com.sportsmatch.application.usecase;

import com.sportsmatch.domain.model.EncuentroDeportivo;

import java.time.LocalDateTime;

public class CrearEncuentroDeportivo {

    public EncuentroDeportivo ejecutar(Long id, String deporte, LocalDateTime fechaHora, String cancha,
                                       int cupoMinimo, int cupoMaximo, String organizador) {
        return new EncuentroDeportivo(id, deporte, fechaHora, cancha, cupoMinimo, cupoMaximo, organizador);
    }
}
