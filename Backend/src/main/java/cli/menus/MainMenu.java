package cli.menus;

import cli.core.Input;
import cli.core.Menu;
import cli.core.MenuRunner;
import cli.core.Screen;
import exception.DomainException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.domain.Categoria;
import model.domain.Descricao;
import model.domain.DetalhesProduto;
import model.domain.Nome;
import model.domain.Preco;
import model.domain.Quantidade;
import model.repositories.Pedido;
import model.repositories.Produto;
import model.repositories.ProdutoPedido;
import model.repositories.Usuario;
import service.PedidoService;
import service.ProdutoService;
import service.RelatorioService;
import service.dto.ItemPedidoRequest;
import service.dto.RelatorioCategoria;
import service.dto.RelatorioFaturamento;

public class MainMenu implements Menu {
    private static final String[] ART = {
            "\033[0;37;40m \033[0m",
            "\033[0;37;40m    \033[0;38;2;172;32;32;40m█\033[0m",
            "\033[0;37;40m   \033[0;38;2;224;103;103;40m█\033[0;38;2;211;69;69;40m█\033[0;38;2;172;32;32;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;224;103;103;40m█\033[0;38;2;213;83;83;40m█\033[0;38;2;211;69;69;40m█\033[0;38;2;192;53;53;40m█\033[0;38;2;172;32;32;40m█\033[0m",
            "\033[0;37;40m \033[0;38;2;213;83;83;40m██\033[0;38;2;211;69;69;40m██\033[0;38;2;192;53;53;40m██\033[0;38;2;128;20;20;40m█\033[0m",
            "\033[0;37;40m \033[0;38;2;172;32;32;40m█\033[0;38;2;192;53;53;40m██\033[0;38;2;179;41;41;40m███\033[0;38;2;109;18;18;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;148;20;20;40m██\033[0;38;2;128;20;20;40m█\033[0;38;2;109;18;18;40m██\033[0m"
    };

    private final MenuRunner runner;
    private final Usuario usuarioLogado;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    private final RelatorioService relatorioService;

    public MainMenu(
        MenuRunner runner,
        Usuario usuarioLogado,
        ProdutoService produtoService,
        PedidoService pedidoService,
        RelatorioService relatorioService
    ) {
        this.runner = runner;
        this.usuarioLogado = usuarioLogado;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
        this.relatorioService = relatorioService;
    }

    @Override
    public String[] banner() { return ART; }

    @Override
    public String title() {
        return "PACT - " + usuarioLogado.ObterNome();
    }

    @Override
    public String[] options() {
        return new String[]{
            "Cadastrar produto",
            "Listar produtos",
            "Criar pedido",
            "Finalizar pedido",
            "Listar pedidos",
            "Relatorios"
        };
    }

    @Override
    public void execute(int escolha, Input input) {
        try {
            switch (escolha) {
                case 1 -> cadastrarProduto(input);
                case 2 -> listarProdutos();
                case 3 -> criarPedido(input);
                case 4 -> finalizarPedido(input);
                case 5 -> listarPedidos();
                case 6 -> relatorios();
            }
        } catch (DomainException | IllegalArgumentException e) {
            Screen.error(e.getMessage());
        } catch (SQLException e) {
            Screen.error("Erro de banco: " + e.getMessage());
        }
    }

    private void cadastrarProduto(Input input) throws SQLException {
        String nome = input.readText("Nome do produto", 100);
        String descricao = input.readText("Descricao", 250);
        double preco = input.readDouble("Preco");
        int estoque = input.readInt("Estoque");
        Categoria categoria = selecionarCategoria(input);

        Produto produto = new Produto(
            null,
            new DetalhesProduto(
                new Nome(nome),
                new Descricao(descricao),
                new Preco(BigDecimal.valueOf(preco))
            ),
            true,
            categoria,
            usuarioLogado.id(), // vendedor = usuário logado
            LocalDateTime.now(),
            new Quantidade(estoque)
        );

        produtoService.registrarProduto(produto);
        Screen.success("Produto \"" + nome + "\" cadastrado!");
    }

    private void listarProdutos() throws SQLException {
        List<Produto> produtos = produtoService.listAllProducts();
        if (produtos.isEmpty()) {
            Screen.warning("Nenhum produto cadastrado.");
            return;
        }
        System.out.println();
        for (Produto p : produtos) {
            System.out.println(
                "  #" + p.id() + "  " + p.getNome() +
                "  |  R$ " + p.getPreco() +
                "  |  estoque: " + p.getQuantidade() +
                "  |  " + p.getNomeCategoria()
            );
        }
    }

    private void criarPedido(Input input) throws SQLException {
        listarProdutos();

        List<ItemPedidoRequest> itens = new ArrayList<>();
        boolean adicionando = true;
        while (adicionando) {
            long idProduto = input.readInt("ID do produto");
            int quantidade = input.readInt("Quantidade");
            itens.add(new ItemPedidoRequest(idProduto, quantidade));

            int cont = input.selectOption(
                new String[]{"Adicionar outro item", "Finalizar carrinho"}
            );
            adicionando = (cont == 1);
        }

        Pedido pedido = pedidoService.criarPedido(usuarioLogado.id(), itens);
        Screen.success(
            "Pedido #" + pedido.id() + " criado com " +
            pedido.itens().size() + " item(ns)."
        );
    }

    private void finalizarPedido(Input input) throws SQLException {
        long id = input.readInt("ID do pedido para finalizar");
        pedidoService.finalizarPedido(id);
        Screen.success(
            "Pedido #" + id +
            " enviado para a FILA. Será processado em segundo plano."
        );
    }

    private void listarPedidos() throws SQLException {
        List<Pedido> pedidos = pedidoService.listarTodos();
        if (pedidos.isEmpty()) {
            Screen.warning("Nenhum pedido cadastrado.");
            return;
        }
        System.out.println();
        for (Pedido p : pedidos) {
            System.out.println(
                "  Pedido #" + p.id() +
                "  |  cliente: " + p.idCliente() +
                "  |  status: " + p.status()
            );
            for (ProdutoPedido item : p.itens()) {
                System.out.println(
                    "      - produto " + item.idProduto() +
                    "  x" + item.getQuantidade() +
                    "  @ R$ " + item.precoUnitario().getValor()
                );
            }
        }
    }

    private void relatorios() throws SQLException {
        System.out.println();
        System.out.println("  == Produtos por categoria ==");
        for (RelatorioCategoria r : relatorioService.produtosPorCategoria()) {
            System.out.println(
                "  " + r.categoria() +
                ": " + r.quantidadeProdutos() + " produto(s)" +
                ", estoque total " + r.totalEstoque() +
                ", preco medio R$ " + r.precoMedio()
            );
        }

        System.out.println();
        System.out.println("  == Faturamento por produto ==");
        List<RelatorioFaturamento> fat = relatorioService.faturamentoPorProduto();
        if (fat.isEmpty()) {
            System.out.println("  (nenhuma venda registrada ainda)");
        }
        for (RelatorioFaturamento r : fat) {
            System.out.println(
                "  " + r.produto() +
                ": vendidos " + r.totalVendido() +
                ", faturamento R$ " + r.faturamento()
            );
        }
    }

    private Categoria selecionarCategoria(Input input) {
        Categoria[] categorias = Categoria.values();
        String[] nomes = new String[categorias.length];
        for (int i = 0; i < categorias.length; i++) {
            nomes[i] = categorias[i].name();
        }
        int escolha = input.selectOption(nomes);
        if (escolha < 1 || escolha > categorias.length) {
            return categorias[0];
        }
        return categorias[escolha - 1];
    }
}
