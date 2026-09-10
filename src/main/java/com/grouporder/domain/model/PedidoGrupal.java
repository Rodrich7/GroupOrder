package com.grouporder.domain.model;

public class PedidoGrupal {

    private Long id;
    private String codigo;
    private EstadoPedido estado;
    private String creador;

    public PedidoGrupal(Long id, String codigo, String creador) {
        this.id = id;
        this.codigo = codigo;
        this.creador = creador;
        this.estado = EstadoPedido.ABIERTO;
    }

    public String getCodigo() {
        return codigo;
    }

    public Long getId() {
        return id;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getCreador() {
        return creador;
    }

    public void cerrar() {
        if (estado != EstadoPedido.ABIERTO) {
            throw new IllegalStateException(
                    "El pedido debe estar ABIERTO para cerrarse"
            );
        }
        estado = EstadoPedido.CERRADO;
    }

    public void confirmar() {
        if (estado != EstadoPedido.CERRADO) {
            throw new IllegalStateException(
                    "El pedido debe estar CERRADO para confirmarse"
            );
        }
        estado = EstadoPedido.CONFIRMADO;
    }

    public void entregar() {
        if (estado != EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException(
                    "El pedido debe estar CONFIRMADO para entregarse"
            );
        }
        estado = EstadoPedido.ENTREGADO;
    }

    public void cancelar() {
        if (estado == EstadoPedido.ENTREGADO ||
                estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException(
                    "El pedido no puede ser cancelado"
            );
        }
        estado = EstadoPedido.CANCELADO;
    }
}