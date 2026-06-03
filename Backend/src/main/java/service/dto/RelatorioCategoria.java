package service.dto;

import java.math.BigDecimal;

/** Linha do relatório "produtos por categoria". */
public record RelatorioCategoria(
    String categoria,
    long quantidadeProdutos,
    long totalEstoque,
    BigDecimal precoMedio
) {}
