package exception;
// Quando DAO retornar null
public class RegisterNotFoundException extends DomainException{

    public RegisterNotFoundException(String entidade, Object id) {
        super(entidade + " com id: " + id + " inválido.");
    }
}
