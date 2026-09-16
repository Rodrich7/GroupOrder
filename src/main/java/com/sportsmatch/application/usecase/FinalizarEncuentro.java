package com.sportsmatch.application.usecase;

import com.sportsmatch.domain.model.EncuentroDeportivo;

public class FinalizarEncuentro {

    public void ejecutar(EncuentroDeportivo encuentro) {
        encuentro.finalizar();
    }
}
