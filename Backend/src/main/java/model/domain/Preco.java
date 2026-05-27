package model.domain;

import java.math.BigDecimal;

public class Preco {

    private final BigDecimal valor;

    public Preco(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser positivo");
        }
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
