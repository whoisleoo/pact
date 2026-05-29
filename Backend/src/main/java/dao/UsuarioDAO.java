package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.domain.Autenticacao;
import model.domain.Email;
import model.domain.Nome;
import model.repositories.Usuario;

public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Usuario usuario) throws SQLException {
        
    }

    public Usuario findById(Long id) throws SQLException {
    String sql = "select id_usuario, nome, e_mail, created_at from usuario where id_usuario = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setLong(1, id);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Usuario(
                    rs.getLong("id_usuario"),
                    new Nome(rs.getString("nome")),
                    new Autenticacao(new Email(rs.getString("e_mail")), null),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        }
    }
    return null;
}

    public Usuario findByEmail(String email) throws SQLException {
    String sql = "select id_usuario, nome, e_mail, created_at from usuario where e_mail = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, email);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Usuario(
                    rs.getLong("id_usuario"),
                    new Nome(rs.getString("nome")),
                    new Autenticacao(new Email(rs.getString("e_mail")), null),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        }
    }
    return null;
}
    public List<Usuario> findAll() throws SQLException {
        return new ArrayList<>();
    }
}