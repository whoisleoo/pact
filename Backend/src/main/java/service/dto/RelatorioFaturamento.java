package service.dto;

import java.math.BigDecimal;

/** Linha do relatório "faturamento por produto". */
public record RelatorioFaturamento(
    String produto,
    long totalVendido,
    BigDecimal faturamento
) {}
