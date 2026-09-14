package com.grouporder.application.usecase;

import com.grouporder.domain.model.PedidoGrupal;

public class EntregarPedidoGrupal {

    public void ejecutar(PedidoGrupal pedido) {
        pedido.entregar();
    }
}
