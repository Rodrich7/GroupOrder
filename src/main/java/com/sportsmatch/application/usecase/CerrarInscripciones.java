package com.sportsmatch.application.usecase;

import com.sportsmatch.domain.model.EncuentroDeportivo;

public class CerrarInscripciones {

    public void ejecutar(EncuentroDeportivo encuentro) {
        encuentro.cerrarInscripciones();
    }
}
