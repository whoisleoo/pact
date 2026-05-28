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

    }

    public Pedido findById(Long id) throws SQLException {
        return null;
    }

    public List<Pedido> findByStatus(String status) throws SQLException {
        return new ArrayList<>();
    }

    public int updateStatus(Long id, String statusAtual, String novoStatus) throws SQLException {
        return 0;
    }
}