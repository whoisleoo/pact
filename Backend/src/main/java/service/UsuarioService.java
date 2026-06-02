package service;

import dao.UsuarioDAO;
import exception.AuthException;
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

    public void registrarUsuario(Usuario usuario) throws SQLException {
        Usuario alreadyExists = usuarioDao.findByEmail(usuario.ObterEmail());

       if(alreadyExists != null){
           throw new DupeRegisterException("Usuario", usuario.ObterEmail());
       }

       usuarioDao.insert(usuario);

    }

public Usuario login(Autenticacao credenciais) throws SQLException{
        Usuario usuario = usuarioDao.findByEmail(credenciais.ObterEmailAsString());

        if(usuario == null){
            throw new AuthException();
        }

        if(!usuario.ValidarSenha(credenciais.extrairSenha())){ // Mudar quando trocar pra SHA256 (moacir falou que n precisa)
            throw new AuthException();
    }
        return usuario;
}

// Função de auxilio
public Usuario findUserById(long id) throws SQLException {
        Usuario usuario = usuarioDao.findById(id);

        if(usuario == null){
            throw new RegisterNotFoundException("Usuario", id);
        }

        return usuario;
    }

}
