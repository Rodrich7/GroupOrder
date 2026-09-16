package com.sportsmatch.application.usecase;

import com.sportsmatch.domain.model.EncuentroDeportivo;

public class CancelarEncuentro {

    public void ejecutar(EncuentroDeportivo encuentro) {
        encuentro.cancelar();
    }
}
