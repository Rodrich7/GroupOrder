package com.sportsmatch.infrastructure.web;

import java.time.LocalDateTime;

public record CrearEncuentroRequest(Long id, String deporte, LocalDateTime fechaHora, String cancha,
                                    int cupoMinimo, int cupoMaximo, String organizador) {
}
