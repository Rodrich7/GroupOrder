package com.grouporder.application.usecase;

import com.grouporder.domain.model.PedidoGrupal;

public class CerrarPedidoGrupal {

    public void ejecutar(PedidoGrupal pedido) {
        pedido.cerrar();
    }
}