package service;

import dao.PedidoDAO;
import dao.ProdutoDAO;
import dao.ProdutoPedidoDAO;
import exception.InsuficientEstock;
import exception.RegisterNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.domain.Preco;
import model.domain.Quantidade;
import model.domain.StatusPedido;
import model.repositories.Pedido;
import model.repositories.Produto;
import model.repositories.ProdutoPedido;
import service.dto.ItemPedidoRequest;

/**
 * Regras de negócio do pedido. É o dono da TRANSAÇÃO: recebe a Connection e
 * monta os três DAOs com ela, garantindo que commit/rollback afetem todos.
 */
public class PedidoService {

    private final Connection connection;
    private final ProdutoDAO produtoDAO;
    private final PedidoDAO pedidoDAO;
    private final ProdutoPedidoDAO itemDAO;

    public PedidoService(Connection connection) {
        this.connection = connection;
        this.produtoDAO = new ProdutoDAO(connection);
        this.pedidoDAO = new PedidoDAO(connection);
        this.itemDAO = new ProdutoPedidoDAO(connection);
    }

    public Pedido criarPedido(Long idCliente, List<ItemPedidoRequest> itensReq)
        throws SQLException {
        if (itensReq == null || itensReq.isEmpty()) {
            throw new IllegalArgumentException(
                "O pedido precisa de pelo menos 1 item."
            );
        }

        // Fase A (fora da transação): buscar produtos, validar existência e
        // capturar o preço atual (snapshot do preço no momento da compra).
        List<Produto> produtos = new ArrayList<>();
        List<ProdutoPedido> itensSemId = new ArrayList<>();
        for (ItemPedidoRequest req : itensReq) {
            Produto p = produtoDAO.findById(req.idProduto());
            if (p == null) {
                throw new RegisterNotFoundException("Produto", req.idProduto());
            }
            produtos.add(p);
            itensSemId.add(
                new ProdutoPedido(
                    p.id(),
                    null,
                    null,
                    new Preco(p.getPreco()),
                    new Quantidade(req.quantidade())
                )
            );
        }

        // Fase B (transação): baixar estoque de todos, inserir pedido e itens.
        connection.setAutoCommit(false);
        try {
            for (int i = 0; i < itensSemId.size(); i++) {
                ProdutoPedido item = itensSemId.get(i);
                int linhas = produtoDAO.decrementarEstoque(
                    item.idProduto(),
                    item.getQuantidade()
                );
                if (linhas == 0) {
                    Produto p = produtos.get(i);
                    throw new InsuficientEstock(
                        p.getNome(),
                        p.getQuantidade(),
                        item.getQuantidade()
                    );
                }
            }

            Pedido pedido = new Pedido(
                null,
                idCliente,
                itensSemId,
                StatusPedido.ABERTO
            );
            Long idPedido = pedidoDAO.insert(pedido);

            List<ProdutoPedido> itensComId = new ArrayList<>();
            for (ProdutoPedido item : itensSemId) {
                itensComId.add(
                    new ProdutoPedido(
                        item.idProduto(),
                        idPedido,
                        null,
                        item.precoUnitario(),
                        item.quantidade()
                    )
                );
            }
            itemDAO.insertBatch(itensComId);

            connection.commit();
            return pedidoDAO.findById(idPedido);
        } catch (SQLException | RuntimeException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /** Usuário finaliza: ABERTO -> FILA (a thread pega daqui). */
    public void finalizarPedido(Long idPedido) throws SQLException {
        int linhas = pedidoDAO.updateStatus(idPedido, "ABERTO", "FILA");
        if (linhas == 0) {
            throw new RegisterNotFoundException("Pedido ABERTO", idPedido);
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        return pedidoDAO.findAll();
    }

    public Pedido findById(Long id) throws SQLException {
        Pedido pedido = pedidoDAO.findById(id);
        if (pedido == null) {
            throw new RegisterNotFoundException("Pedido", id);
        }
        return pedido;
    }
}
