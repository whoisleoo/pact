package exception;
// Base do exception, oque vai retornar basicamente
public class DomainException extends RuntimeException {
    public DomainException(String message){
        super(message);
    }
}


/*
*             Mensagem para a equipe: COMO USAR AS EXCEPTIONS CUSTOMIZADAS
*
*             1. Antes do exception:
*             throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
*
*             2. Agora com exception:
*             throw new ValidacaoException("email", "formato inválido");
*
*             Lembrem de usar diferentes de exceptions pra diferentes problemas.
*
 */
