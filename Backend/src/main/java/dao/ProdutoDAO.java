package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.domain.*;
import model.domain.DetalhesProduto;
import model.repositories.*;
import model.repositories.Produto;

public class ProdutoDAO {

    private final Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Produto produto) throws SQLException {
        String sql =
            "insert into produto (nome, preco, ativo, descricao, categoria, id_vendedor, estoque) values (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setBoolean(3, produto.Ativo());
            stmt.setString(4, produto.getDescricao());
            stmt.setString(5, produto.getNomeCategoria());
            stmt.setLong(6, produto.idVendedor());
            stmt.setInt(7, produto.getQuantidade());

            stmt.executeUpdate();
        }
    }

    public Produto findById(Long id) throws SQLException {
        String sql = "select * from produto where id_produto = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduto(rs);
                }
            }
        }
        return null;
    }

    public List<Produto> findAll() throws SQLException {
        String sql = "select * from produto";

        List<Produto> produtos = new ArrayList<>();

        try (
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }
        }
        return produtos;
    }

    public List<Produto> findByCategoria(String categoria) throws SQLException {
        String sql = "select * from produto where categoria = ?";
        List<Produto> produtos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapResultSetToProduto(rs));
                }
            }
        }
        return produtos;
    }

    public int decrementarEstoque(Long id, int quantidade) throws SQLException {
        String sql =
            "UPDATE produto SET estoque = estoque - ? WHERE id_produto = ? AND estoque >= ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setLong(2, id);
            stmt.setInt(3, quantidade);
        }

        return 0;
    }

    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        return new Produto(
            rs.getLong("id_produto"),

            new DetalhesProduto(
                new Nome(rs.getString("nome")),
                new Descricao(rs.getString("descricao")),
                new Preco(rs.getBigDecimal("preco"))
            ),

            rs.getBoolean("ativo"),

            Categoria.deString(rs.getString("categoria")), // Convertendo String p/

            rs.getLong("id_vendedor"),

            rs.getTimestamp("created_at").toLocalDateTime(), // Timestamp p/ LocalDateTime

            new Quantidade(rs.getInt("estoque")) // Supondo que você tenha essa coluna
        );
    }
}
