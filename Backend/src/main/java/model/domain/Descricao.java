package model.domain;

public class Descricao {

    private final String descricao;

    public Descricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException(
                "Descrição não pode ser nula ou vazia"
            );
        }
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
