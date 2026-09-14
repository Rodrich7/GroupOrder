package com.grouporder.application.usecase;

import com.grouporder.domain.model.PedidoGrupal;

public class ConfirmarPedidoGrupal {

    public void ejecutar(PedidoGrupal pedido) {
        pedido.confirmar();
    }
}
