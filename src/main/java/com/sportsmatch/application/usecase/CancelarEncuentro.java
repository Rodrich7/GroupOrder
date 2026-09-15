package com.grouporder.application.usecase;

import com.grouporder.domain.model.PedidoGrupal;

public class CancelarPedidoGrupal {

    public void ejecutar(PedidoGrupal pedido) {
        pedido.cancelar();
    }
}
