package service.dto;

/**
 * Representa "o que comprar" antes do pedido existir: um produto e a
 * quantidade desejada. Não tem id de pedido nem preço — esses só passam a
 * existir no momento da persistência.
 */
public record ItemPedidoRequest(Long idProduto, int quantidade) {}
