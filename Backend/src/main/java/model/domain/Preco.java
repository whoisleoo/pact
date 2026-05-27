package model.domain;

import java.math.BigDecimal;

public class Preco {
    private final BigDecimal valor;

    public Preco(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) < 0 || valor == null) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
