package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.repositories.Pedido;

public class PedidoDAO {

    private final Connection connection;

    public PedidoDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (id_cliente, id_vendedor, status) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, pedido.idCliente());
            stmt.setLong(2, pedido.idVendedor());
            stmt.setString(3, pedido.status().name());

            stmt.executeUpdate();
        }
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

    public List<Pedido> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE status = ?";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                pedidos = mapResultSetToPedidos(rs);
            }
        }
        
        return new ArrayList<>();
    }

    public int updateStatus(Long id, String statusAtual, String novoStatus) throws SQLException {
        String sql = "UPDATE pedido SET status = ? WHERE id_pedido = ? AND status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setLong(2, id);
            stmt.setString(3, statusAtual);

            return stmt.executeUpdate();
        }
    }

    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        return new Pedido(
            rs.getLong("id_pedido"),
            rs.getLong("id_cliente"),
            rs.getLong("id_vendedor"),
            null, 
            model.domain.StatusPedido.valueOf(rs.getString("status"))
        );
    }

    private List<Pedido> mapResultSetToPedidos(ResultSet rs) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        while (rs.next()) {
            pedidos.add(mapResultSetToPedido(rs));
        }
        return pedidos;
    }
}
