package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.repositories.ProdutoPedido;

public class ProdutoPedidoDAO {

    private final Connection connection;

    public ProdutoPedidoDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertBatch(List<ProdutoPedido> itens) throws SQLException {

    }

    public List<ProdutoPedido> findByPedido(Long idPedido) throws SQLException {
        return new ArrayList<>();
    }
}