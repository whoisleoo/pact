package exception;
// Para usar essa aqui usa o getnome, getquantidade etc e passa nos parametros.
public class InsuficientEstock extends DomainException{
    public InsuficientEstock(String produto, int disponivel, int solicitado) {
        super("Estoque insuficiente para " + produto + " disponível " + disponivel + " solicitado " + solicitado);
    }
}
