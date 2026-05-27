package model.domain;

public class Email {
    private final String email;

    public Email(String email) {
        if (email == null || email.isBlank() || !isValidEmail(email)) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
