package model.domain;

public class Quantidade {
    private final int valor;

    public Quantidade(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.valor = valor;
    }

    public int getQuantidade() {
        return valor;
    }
}
