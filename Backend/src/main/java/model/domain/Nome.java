package model.domain;

public class Nome {

    private final String nome;

    public Nome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "Nome não pode ser nulo ou vazio"
            );
        }
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
