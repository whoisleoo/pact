package service;

import dao.UsuarioDAO;
import exception.AuthException;
import exception.DatabaseException;
import exception.DupeRegisterException;
import exception.RegisterNotFoundException;
import model.domain.Autenticacao;
import model.repositories.Usuario;
import java.sql.SQLException;

public class UsuarioService {
    private final UsuarioDAO usuarioDao;

    public UsuarioService(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public void registrarUsuario(Usuario usuario) {
        try {
            Usuario alreadyExists = usuarioDao.findByEmail(usuario.ObterEmail());

            if (alreadyExists != null) {
                throw new DupeRegisterException("Usuario", usuario.ObterEmail());
            }

            usuarioDao.insert(usuario);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao registrar usuário", e);
        }
    }

    public Usuario login(Autenticacao credenciais) {
        try {
            Usuario usuario = usuarioDao.findByEmail(credenciais.ObterEmailAsString());

            if (usuario == null) {
                throw new AuthException();
            }

            if (!usuario.ValidarSenha(credenciais.extrairSenha())) {
                throw new AuthException();
            }
            return usuario;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao realizar login", e);
        }
    }

    public Usuario findUserById(long id) {
        try {
            Usuario usuario = usuarioDao.findById(id);

            if (usuario == null) {
                throw new RegisterNotFoundException("Usuario", id);
            }

            return usuario;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por id", e);
        }
    }
}
