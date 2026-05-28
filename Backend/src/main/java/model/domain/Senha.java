package model.domain;

public class Senha {

    private final String senha;

    public Senha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException(
                "Senha deve ter ao menos 8 caracteres"
            );
        }
        if (!senha.matches(".*[A-Za-z].*") || !senha.matches(".*\\d.*")) {
            throw new IllegalArgumentException(
                "Senha deve conter letra e número"
            );
        }
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "***";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Senha other)) return false;
        return this.senha.equals(other.senha);
    }

    @Override
    public int hashCode() {
        return senha.hashCode();
    }
}
