package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.repositories.Produto;

public class ProdutoDAO {

    private final Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Produto produto) throws SQLException {

    }

    public Produto findById(Long id) throws SQLException {
        return null;
    }

    public List<Produto> findAll() throws SQLException {
        return new ArrayList<>();
    }

    public List<Produto> findByCategoria(String categoria) throws SQLException {
        return new ArrayList<>();
    }

    public int decrementarEstoque(Long id, int quantidade) throws SQLException {
        return 0;
    }
}