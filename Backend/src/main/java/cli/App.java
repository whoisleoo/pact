package cli;

import cli.core.Input;
import cli.core.MenuRunner;
import cli.menus.AuthMenu;
import dao.ProdutoDAO;
import dao.RelatorioDAO;
import dao.UsuarioDAO;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;
import service.PedidoService;
import service.ProdutoService;
import service.RelatorioService;
import service.UsuarioService;
import thread.ProcessadorPedidos;
import util.ConnectionFactory;

public class App {

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        // Conexão do menu (a thread usa as suas próprias).
        Connection conn = ConnectionFactory.getConnection();

        // Wiring manual das dependências (DI sem framework).
        UsuarioService usuarioService = new UsuarioService(new UsuarioDAO(conn));
        ProdutoService produtoService = new ProdutoService(new ProdutoDAO(conn));
        PedidoService pedidoService = new PedidoService(conn);
        RelatorioService relatorioService = new RelatorioService(
            new RelatorioDAO(conn)
        );

        // Thread de processamento roda em segundo plano (daemon: morre com o app).
        Thread processador = new Thread(
            new ProcessadorPedidos(),
            "processador-pedidos"
        );
        processador.setDaemon(true);
        processador.start();

        Scanner scanner = new Scanner(System.in, "UTF-8");
        Input input = new Input(scanner);
        MenuRunner runner = new MenuRunner(input);

        runner.execute(
            new AuthMenu(
                runner,
                usuarioService,
                produtoService,
                pedidoService,
                relatorioService
            )
        );

        scanner.close();
        conn.close();
    }
}
