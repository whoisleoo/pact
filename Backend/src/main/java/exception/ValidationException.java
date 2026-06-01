package exception;
// Pra exceptions do tipo illegalargument em email, senha, nome
public class ValidationException extends DomainException{
    public ValidationException(String campo, String motivo) {
        super(campo + ": " + motivo);
    }
}
