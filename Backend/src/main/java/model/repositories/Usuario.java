package model.repositories;

import java.time.LocalDateTime;
import model.domain.Autenticacao;
import model.domain.Nome;

public record Usuario(
    Long id,
    Nome nome,
    Autenticacao autenticacao,
    LocalDateTime createdAt
) {
    public Usuario(Nome nome, Autenticacao autenticacao) {
        this(null, nome, autenticacao, LocalDateTime.now());
    }

    public String ObterEmail() {
        return autenticacao.ObterEmailAsString();
    }

    public String ObterNome() {
        return nome.toString();
    }

    public String ObterSenhaBanco() {
        return autenticacao.extrairSenha();
    }
}
