package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.repositories.Usuario;

public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Usuario usuario) throws SQLException {

    }

    public Usuario findById(Long id) throws SQLException {
        return null;
    }

    public Usuario findByEmail(String email) throws SQLException {
        return null;
    }

    public List<Usuario> findAll() throws SQLException {
        return new ArrayList<>();
    }
}