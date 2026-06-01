package model.domain;

import java.math.BigDecimal;

public record DetalhesProduto(Nome nome, Descricao descricao, Preco preco) {
    public String getNome() {
        return nome.toString();
    }

    public String getDescricao() {
        return descricao.toString();
    }

    public BigDecimal getPreco() {
        return preco.getValor();
    }
}
