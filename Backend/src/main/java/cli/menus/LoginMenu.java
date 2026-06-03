package cli.menus;

import cli.core.Form;
import cli.core.Input;
import cli.core.MenuRunner;
import cli.core.Screen;
import exception.DomainException;
import java.sql.SQLException;
import model.domain.Autenticacao;
import model.domain.Email;
import model.domain.Senha;
import model.repositories.Usuario;
import service.PedidoService;
import service.ProdutoService;
import service.RelatorioService;
import service.UsuarioService;

public class LoginMenu implements Form {

    private static final String[] ART = {
            "\033[0;37;40m \033[0m",
            "\033[0;37;40m   \033[0;38;2;209;35;35;40m                  \033[0;38;2;181;48;48;40m▟▀▀▙\033[0m",
            "\033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;209;35;35;40m \033[0;37;40m    \033[0;38;2;209;35;35;40m \033[0;37;40m      \033[0;38;2;209;35;35;40m   \033[0;38;2;181;48;48;40m▌\033[0;37;40m  \033[0;38;2;181;48;48;40m▐\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m      \033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m     \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;181;48;48;40m██▛▜\033[0;38;2;215;66;66;40m█\033[0;38;2;181;48;48;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m  \033[0;37;40m \033[0;38;2;209;35;35;40m   \033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m     \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;181;48;48;40m██\033[0;38;2;209;35;35;40m \033[0;38;2;181;48;48;40m \033[0;38;2;195;50;50;40m█\033[0;38;2;181;48;48;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m      \033[0;37;40m   \033[0;38;2;209;35;35;40m    \033[0;37;40m   \033[0;38;2;209;35;35;40m  \033[0;38;2;181;48;48;40m███\033[0;38;2;143;25;25;40m███\033[0m"
    };

    private final MenuRunner runner;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    private final RelatorioService relatorioService;

    public LoginMenu(
        MenuRunner runner,
        UsuarioService usuarioService,
        ProdutoService produtoService,
        PedidoService pedidoService,
        RelatorioService relatorioService
    ) {
        this.runner = runner;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
        this.relatorioService = relatorioService;
    }

    @Override
    public String[] banner() { return ART; }

    @Override
    public String title() {
        return "🩸 PACT - Login";
    }

    @Override
    public String[] fields() {
        return new String[]{"Email", "Senha"};
    }

    @Override
    public int[] passwordFields() {
        return new int[]{1};
    }

    @Override
    public int[] fieldLimits() {
        return new int[]{ 40, 20 };
    }

    @Override
    public void submit(String[] values, Input input) {
        try {
            Autenticacao credenciais = new Autenticacao(
                new Email(values[0]),
                new Senha(values[1])
            );
            Usuario usuario = usuarioService.login(credenciais);
            Screen.success("Bem-vindo, " + usuario.ObterNome() + "!");

            runner.execute(
                new MainMenu(
                    runner,
                    usuario,
                    produtoService,
                    pedidoService,
                    relatorioService
                )
            );
        } catch (DomainException | IllegalArgumentException e) {
            Screen.error(e.getMessage());
        } catch (SQLException e) {
            Screen.error("Erro de banco: " + e.getMessage());
        }
    }
}
