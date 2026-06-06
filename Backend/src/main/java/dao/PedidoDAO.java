package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.domain.StatusPedido;
import model.repositories.Pedido;
import model.repositories.ProdutoPedido;

public class PedidoDAO {

    private final Connection connection;

    public PedidoDAO(Connection connection) {
        this.connection = connection;
    }

    public Long insert(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (id_cliente, status) VALUES (?, ?)";

        try (
            PreparedStatement stmt = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            stmt.setLong(1, pedido.idCliente());
            stmt.setString(2, pedido.status().name());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("Falha ao obter o id gerado do pedido.");
    }

    public Pedido findById(Long id) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPedido(rs);
                }
            }
        }
        return null;
    }

    public List<Pedido> findAll() throws SQLException {
        String sql = "SELECT * FROM pedido ORDER BY id_pedido";
        return queryPedidos(sql, null);
    }

    public List<Pedido> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE status = ? ORDER BY id_pedido";
        return queryPedidos(sql, status);
    }

    public int updateStatus(Long id, String statusAtual, String novoStatus)
        throws SQLException {
        String sql = "UPDATE pedido SET status = ? WHERE id_pedido = ? AND status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setLong(2, id);
            stmt.setString(3, statusAtual);

            return stmt.executeUpdate();
        }
    }

    // Executa em duas fases (lê os ids, fecha o ResultSet, depois carrega os
    // itens) para evitar abrir uma nova query enquanto o ResultSet de fora
    // ainda está aberto na mesma Connection.
    private List<Pedido> queryPedidos(String sql, String statusFiltro)
        throws SQLException {
        List<Long> ids = new ArrayList<>();
        List<String> statuses = new ArrayList<>();
        List<Long> clientes = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (statusFiltro != null) stmt.setString(1, statusFiltro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong("id_pedido"));
                    clientes.add(rs.getLong("id_cliente"));
                    statuses.add(rs.getString("status"));
                }
            }
        }

        List<Pedido> pedidos = new ArrayList<>();
        ProdutoPedidoDAO itemDAO = new ProdutoPedidoDAO(connection);
        for (int i = 0; i < ids.size(); i++) {
            List<ProdutoPedido> itens = itemDAO.findByPedido(ids.get(i));
            pedidos.add(
                new Pedido(
                    ids.get(i),
                    clientes.get(i),
                    itens,
                    StatusPedido.valueOf(statuses.get(i))
                )
            );
        }
        return pedidos;
    }

    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Long idPedido = rs.getLong("id_pedido");
        List<ProdutoPedido> itens = new ProdutoPedidoDAO(connection)
            .findByPedido(idPedido);
        return new Pedido(
            idPedido,
            rs.getLong("id_cliente"),
            itens,
            StatusPedido.valueOf(rs.getString("status"))
        );
    }
}
