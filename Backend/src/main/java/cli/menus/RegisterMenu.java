package cli.menus;

import cli.core.Form;
import cli.core.Input;
import cli.core.Screen;
import exception.DatabaseException;
import exception.DomainException;
import model.domain.Autenticacao;
import model.domain.Email;
import model.domain.Nome;
import model.domain.Senha;
import model.repositories.Usuario;
import service.UsuarioService;

public class RegisterMenu implements Form {

    private static final String[] ART = {
            "\033[0;37;40m \033[0m",
            "\033[0;37;40m   \033[0;38;2;209;35;35;40m                  \033[0;38;2;181;48;48;40m▟▀▀▙\033[0m",
            "\033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;209;35;35;40m \033[0;37;40m    \033[0;38;2;209;35;35;40m \033[0;37;40m      \033[0;38;2;209;35;35;40m   \033[0;38;2;181;48;48;40m▌\033[0;37;40m  \033[0;38;2;181;48;48;40m▐\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m      \033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m     \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;181;48;48;40m██▛▜\033[0;38;2;215;66;66;40m█\033[0;38;2;181;48;48;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m  \033[0;37;40m \033[0;38;2;209;35;35;40m   \033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m     \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;181;48;48;40m██\033[0;38;2;209;35;35;40m \033[0;38;2;181;48;48;40m \033[0;38;2;195;50;50;40m█\033[0;38;2;181;48;48;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m      \033[0;37;40m   \033[0;38;2;209;35;35;40m    \033[0;37;40m   \033[0;38;2;209;35;35;40m  \033[0;38;2;181;48;48;40m███\033[0;38;2;143;25;25;40m███\033[0m"
    };

    private final UsuarioService usuarioService;

    public RegisterMenu(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public String[] banner() { return ART; }

    @Override
    public String title() {
        return "🩸 PACT - Register";
    }

    @Override
    public String[] fields() {
        return new String[]{"Nome", "Senha", "Email"};
    }

    @Override
    public int[] passwordFields() {
        return new int[]{1};
    }

    @Override
    public int[] fieldLimits() {
        return new int[]{ 20, 20, 40 };
    }

    @Override
    public void submit(String[] values, Input input) {
        try {
            Usuario usuario = new Usuario(
                new Nome(values[0]),
                new Autenticacao(new Email(values[2]), new Senha(values[1]))
            );
            usuarioService.registrarUsuario(usuario);
            Screen.success("Usuário registrado! Agora faça login.");
        } catch (DomainException | IllegalArgumentException | DatabaseException e) {
            Screen.error(e.getMessage());
        }
    }
}
