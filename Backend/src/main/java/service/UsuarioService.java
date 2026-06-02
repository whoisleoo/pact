package service;

import dao.UsuarioDAO;
import exception.AuthException;
import exception.DupeRegisterException;
import exception.RegisterNotFoundException;
import model.domain.Autenticacao;
import model.domain.Email;
import model.domain.Nome;
import model.domain.Senha;
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

public Usuario login(String email, Senha tentativa) throws SQLException{
        Usuario usuario = usuarioDao.findByEmail(email);

        if(usuario == null || !usuario.autenticacao().autenticar(tentativa)){ // Mudar quando trocar pra SHA256
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
