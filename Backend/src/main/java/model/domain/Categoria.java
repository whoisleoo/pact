package model.domain;

public enum Categoria {
    DEV,
    MARKETING,
    VIDEOS,
    DESIGN;

    public static Categoria deString(String valorDoBanco) {
        if (valorDoBanco == null || valorDoBanco.isBlank()) {
            return DEV;
        }

        try {
            return Categoria.valueOf(valorDoBanco.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DEV;
        }
    }
}
