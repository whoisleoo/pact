package model.domain;

public class Senha {
    private final String senha;

    public Senha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        this.senha = senha;
    }

    @Override
    public String toString() {
        return senha;
    }
}
