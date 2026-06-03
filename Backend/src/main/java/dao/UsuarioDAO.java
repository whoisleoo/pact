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
import model.domain.Senha;
import model.repositories.Usuario;

public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Usuario usuario) throws SQLException {
        String sql =
            "insert into usuario (nome, e_mail, senha) values (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.ObterNome());
            stmt.setString(2, usuario.ObterEmail());
            stmt.setString(3, usuario.ObterSenhaBanco()); // Implementar hash code pode sha-256 em breve.

            stmt.executeUpdate();
        }
    }

    public Usuario findById(Long id) throws SQLException {
        String sql =
            "select id_usuario, nome, e_mail, created_at from usuario where id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        return null;
    }

    public Usuario findByEmail(String email) throws SQLException {
        String sql =
            "select id_usuario, nome, e_mail, senha, created_at from usuario where e_mail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuarioComSenha(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> findAll() throws SQLException {
        String sql = "select id_usuario, nome, e_mail, created_at from usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        }
        return usuarios;
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getLong("id_usuario"),
            new Nome(rs.getString("nome")),
            new Autenticacao(new Email(rs.getString("e_mail")), null),
            rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    private Usuario mapResultSetToUsuarioComSenha(ResultSet rs)
        throws SQLException {
        return new Usuario(
            rs.getLong("id_usuario"),
            new Nome(rs.getString("nome")),
            new Autenticacao(
                new Email(rs.getString("e_mail")),
                new Senha(rs.getString("senha"))
            ),
            rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
