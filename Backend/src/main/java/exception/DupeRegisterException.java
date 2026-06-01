package exception;
// Email duplicado na hora do cadastro
public class DupeRegisterException extends DomainException {
    public DupeRegisterException(String entidade, String campo) {
        super(entidade + " com: " + campo + " já cadastrado.");

    }
}
