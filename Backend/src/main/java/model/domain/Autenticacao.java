package model.domain;

public class Autenticacao {

    private final Email email;
    private final Senha senha;

    public Autenticacao(Email email, Senha senha) {
        this.email = email;
        this.senha = senha;
    }

    public String ObterEmailAsString() {
        return email.toString();
    }

    public String extrairSenha() {
        return senha.senhaBanco();
    }

    public boolean autenticar(Senha tentativa) {
        return this.senha.equals(tentativa);
    }
}
