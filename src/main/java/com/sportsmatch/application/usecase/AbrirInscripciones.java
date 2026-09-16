package com.sportsmatch.application.usecase;

import com.sportsmatch.domain.model.EncuentroDeportivo;

public class AbrirInscripciones {

    public void ejecutar(EncuentroDeportivo encuentro) {
        encuentro.abrirInscripciones();
    }
}
