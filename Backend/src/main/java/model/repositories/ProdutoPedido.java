package model.repositories;

import model.domain.Preco;
import model.domain.Quantidade;

public record ProdutoPedido(
    Long idProduto,
    Long idPedido,
    Long idProdutoPedido,
    Preco precoUnitario,
    Quantidade quantidade
) {
    public int getQuantidade() {
        return this.quantidade.getQuantidade();
    }
}
