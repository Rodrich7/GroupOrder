package com.grouporder.domain.model;

public class PedidoGrupal {

    private Long id;
    private String codigo;
    private EstadoPedido estado;
    private String creador;

    public PedidoGrupal(Long id, String codigo, EstadoPedido estado, String creador) {
        this.id = id;
        this.codigo = codigo;
        this.estado = estado;
        this.creador = creador;
    }

}
