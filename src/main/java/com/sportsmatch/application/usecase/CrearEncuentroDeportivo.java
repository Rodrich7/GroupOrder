package com.grouporder.application.usecase;

import com.grouporder.domain.model.PedidoGrupal;

public class CrearPedidoGrupal {

    public PedidoGrupal ejecutar(Long id, String codigo, String creador) {
        return new PedidoGrupal(id, codigo, creador);
    }
}
