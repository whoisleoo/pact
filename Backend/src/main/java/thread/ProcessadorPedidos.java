package thread;

import model.domain.StatusPedido;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Thread de background que processa pedidos em FILA sem travar o menu principal.
 *
 * FILA -> PROCESSANDO -> FINALIZADO
 */
public class ProcessadorPedidos implements Runnable {

    private static final long INTERVALO_ENTRE_BUSCAS_MS = 5_000;
    private static final long TEMPO_PROCESSAMENTO_MS = 3_000;

    private volatile boolean rodando = true;

    @Override
    public void run() {
        log("iniciada.");

        while (rodando && !Thread.currentThread().isInterrupted()) {
            try {
                boolean processouAlgumPedido = processarProximoPedido();

                if (!processouAlgumPedido) {
                    dormir(INTERVALO_ENTRE_BUSCAS_MS);
                }
            } catch (SQLException e) {
                log("erro de banco: " + e.getMessage());
                dormir(INTERVALO_ENTRE_BUSCAS_MS);
            }
        }

        log("encerrada.");
    }

    public void parar() {
        rodando = false;
    }

    private boolean processarProximoPedido() throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Long idPedido = buscarPedidoEmFila(connection);

            if (idPedido == null) {
                return false;
            }

            boolean conseguiuReservar = marcarComoProcessando(connection, idPedido);

            if (!conseguiuReservar) {
                return false;
            }

            log("processando pedido #" + idPedido + "...");
            dormir(TEMPO_PROCESSAMENTO_MS);

            finalizarPedido(connection, idPedido);
            log("pedido #" + idPedido + " finalizado.");

            return true;
        }
    }

    private Long buscarPedidoEmFila(Connection connection) throws SQLException {
        String sql = """
                SELECT id_pedido
                FROM pedido
                WHERE status = ?
                ORDER BY id_pedido
                LIMIT 1
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, StatusPedido.FILA.name());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_pedido");
                }
            }
        }

        return null;
    }

    private boolean marcarComoProcessando(Connection connection, Long idPedido) throws SQLException {
        String sql = """
                UPDATE pedido
                SET status = ?
                WHERE id_pedido = ?
                AND status = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, StatusPedido.PROCESSANDO.name());
            stmt.setLong(2, idPedido);
            stmt.setString(3, StatusPedido.FILA.name());

            return stmt.executeUpdate() == 1;
        }
    }

    private void finalizarPedido(Connection connection, Long idPedido) throws SQLException {
        String sql = """
                UPDATE pedido
                SET status = ?
                WHERE id_pedido = ?
                AND status = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, StatusPedido.FINALIZADO.name());
            stmt.setLong(2, idPedido);
            stmt.setString(3, StatusPedido.PROCESSANDO.name());
            stmt.executeUpdate();
        }
    }

    private void dormir(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            rodando = false;
        }
    }

    private void log(String mensagem) {
        System.out.println("[THREAD PEDIDOS] " + mensagem);
    }
}
