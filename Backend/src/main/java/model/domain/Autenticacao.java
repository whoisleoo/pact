package model.domain;

public class Autenticacao {
    private final Email email;
    private final Senha senha;

    public Autenticacao(Email email, Senha senha) {
        this.email = email;
        this.senha = senha;
    }

    public Email getEmail() {
        return email;
    }
}
