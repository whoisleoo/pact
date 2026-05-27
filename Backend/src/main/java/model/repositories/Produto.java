package model.repositories;
// classe contem id, nome, preco, isAtivo, descricao, categoria, id_vendedor, createdAT

// ID, isAtivo e createdAT são gerados automaticamente
// Nome, preco, descricao, categoria e id_vendedor são passados como um parametro unico, ProdutoRequest.

import java.time.LocalDateTime;

import model.domain.Categoria;
import model.domain.DetalhesProduto;
import model.domain.Quantidade;

public record Produto(
        Long id,
        DetalhesProduto detalhes,
        Boolean isAtivo,
        Categoria categoria,
        Long idVendedor,
        LocalDateTime createdAt,
        Quantidade quantidadeEstoque) {
    public Produto {
        if (isAtivo == null)
            isAtivo = true;
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
