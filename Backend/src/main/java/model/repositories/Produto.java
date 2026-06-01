package model.repositories;

// classe contem id, nome, preco, isAtivo, descricao, categoria, id_vendedor, createdAT

import java.math.BigDecimal;
// ID, isAtivo e createdAT são gerados automaticamente
// Nome, preco, descricao, categoria e id_vendedor são passados como um parametro unico, ProdutoRequest.

import java.time.LocalDateTime;
import model.domain.Categoria;
import model.domain.DetalhesProduto;
import model.domain.Quantidade;

public record Produto(
    Long id,
    DetalhesProduto detalhes, // detalhes = nome, descricao, preco
    Boolean isAtivo,
    Categoria categoria,
    Long idVendedor,
    LocalDateTime createdAt,
    Quantidade quantidadeEstoque
) {
    public Produto {
        if (isAtivo == null) isAtivo = true;
    }

    public String getNome() {
        return detalhes.getNome();
    }

    public int getQuantidade() {
        return quantidadeEstoque.getQuantidade();
    }

    public BigDecimal getPreco() {
        return detalhes.getPreco();
    }

    public String getDescricao() {
        return detalhes.getDescricao();
    }

    public Boolean Ativo() {
        return this.isAtivo;
    }

    public String getNomeCategoria() {
        return this.categoria.name();
    }
}
