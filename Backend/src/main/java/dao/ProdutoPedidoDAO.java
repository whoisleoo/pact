package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.domain.Preco;
import model.domain.Quantidade;
import model.repositories.ProdutoPedido;

public class ProdutoPedidoDAO {

    private final Connection connection;

    public ProdutoPedidoDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertBatch(List<ProdutoPedido> itens) throws SQLException {
        String sql =
            "INSERT INTO produto_pedido (id_pedido, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (ProdutoPedido item : itens) {
                stmt.setLong(1, item.idPedido());
                stmt.setLong(2, item.idProduto());
                stmt.setInt(3, item.getQuantidade());
                stmt.setBigDecimal(4, item.precoUnitario().getValor());

                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }

    public List<ProdutoPedido> findByPedido(Long idPedido) throws SQLException {
        String sql = "select * from produto_pedido where id_pedido = ?";
        List<ProdutoPedido> produtos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(
                        new ProdutoPedido(
                            rs.getLong("id_produto"),
                            rs.getLong("id_pedido"),
                            rs.getLong("id_produto_pedido"),
                            new Preco(rs.getBigDecimal("preco_unitario")),
                            new Quantidade(rs.getInt("quantidade"))
                        )
                    );
                }
            }
        }

        return produtos;
    }
}
