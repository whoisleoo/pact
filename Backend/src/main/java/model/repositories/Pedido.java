package model.repositories;

import java.util.List;
import model.domain.StatusPedido;

// id, status (enum: FILA, PROCESSAMENTO, ENTREGUE), id cliente, id vendedor

public record Pedido(
    Long id,
    Long idCliente,
    Long idVendedor,
    List<ProdutoPedido> itens,
    StatusPedido status
) {
    public Pedido {
        if (status == null) status = StatusPedido.ABERTO;
        if (
            itens == null || itens.isEmpty()
        ) throw new IllegalArgumentException(
            "Pedido precisa de pelo menos 1 item."
        );
        itens = List.copyOf(itens);
    }
}
