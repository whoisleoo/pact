package model.domain;

public class Nome {

    private final String nome;

    public Nome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "Nome não pode ser nulo ou vazio"
            );


        }

        if(nome.length() < 3 || nome.length() > 150){
            throw new IllegalArgumentException(
                    "Nome inválido."
            );
        }

        
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
