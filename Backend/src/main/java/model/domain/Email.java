package model.domain;

import exception.ValidationException;

import java.util.regex.Pattern;

public class Email {

    private final String email;

    public Email(String email) {
        if (email == null || email.isBlank() || !isValidEmail(email)) {
            throw new ValidationException("Email", "Email vázio ou inválido.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^(?=.{1,254}$)(?=.{1,64}@)[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$"
    );
}
