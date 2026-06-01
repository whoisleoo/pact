package exception;
// Login inválido
public class AuthException extends DomainException {
    public AuthException() {
        super("Credenciais Inválidas.");
    }
}
