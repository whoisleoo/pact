package thread;

import dao.PedidoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.repositories.Pedido;
import util.ConnectionFactory;

/**
 * Thread de background que processa pedidos.
 *
 * A cada ciclo: abre a SUA PRÓPRIA conexão (isolada da do menu), procura um
 * pedido na FILA, tenta "reivindicá-lo" mudando o status para PROCESSANDO de
 * forma atômica (UPDATE condicional — garante que outra thread não pegue o
 * mesmo), simula o processamento e finaliza. Fecha a conexão ao fim do ciclo.
 */
public class ProcessadorPedidos implements Runnable {

    private static final long INTERVALO_MS = 2000;
    private static final long TEMPO_PROCESSAMENTO_MS = 3000;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                processarUmCiclo();
                Thread.sleep(INTERVALO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                // Não derruba a thread por uma falha pontual de banco ou erro em runtime (ex: NPE).
                System.err.println(
                    "[ProcessadorPedidos] erro inesperado: " + e.getMessage()
                );
            }
        }
    }

    private void processarUmCiclo() throws SQLException, InterruptedException {
        // Conexão própria, aberta e fechada dentro do ciclo.
        try (Connection conn = ConnectionFactory.getConnection()) {
            PedidoDAO pedidoDAO = new PedidoDAO(conn);

            List<Pedido> naFila = pedidoDAO.findByStatus("FILA");
            if (naFila.isEmpty()) return;

            Pedido pedido = naFila.get(0);

            // Claim atômico: só uma thread consegue afetar a linha.
            int reivindicado = pedidoDAO.updateStatus(
                pedido.id(),
                "FILA",
                "PROCESSANDO"
            );
            if (reivindicado == 0) return; // outra thread pegou antes

            System.out.println(
                "\n[ProcessadorPedidos] processando pedido #" + pedido.id() +
                "..."
            );

            // Simula o trabalho.
            Thread.sleep(TEMPO_PROCESSAMENTO_MS);

            pedidoDAO.updateStatus(pedido.id(), "PROCESSANDO", "FINALIZADO");
            System.out.println(
                "[ProcessadorPedidos] pedido #" + pedido.id() + " FINALIZADO."
            );
        }
    }
}
