package com.sportsmatch.application.usecase;

import com.sportsmatch.domain.model.EncuentroDeportivo;

public class IniciarEncuentro {

    public void ejecutar(EncuentroDeportivo encuentro) {
        encuentro.iniciar();
    }
}
