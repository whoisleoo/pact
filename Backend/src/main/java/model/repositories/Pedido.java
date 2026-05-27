package model.repositories;

import model.domain.FilaPedido;

// id, status (enum: FILA, PROCESSAMENTO, ENTREGUE), id cliente, id vendedor

public record Pedido(
        Long id,
        FilaPedido status,
        Long idCliente,
        Long idVendedor) {
    public Pedido {
        if (status == null)
            status = FilaPedido.FILA;
    }
}
