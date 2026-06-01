package cli.menus;

import cli.core.Form;
import cli.core.Input;

public class RegisterMenu implements Form {

    private static final String[] ART = {
            "\033[0;37;40m \033[0m",
            "\033[0;37;40m   \033[0;38;2;209;35;35;40m                  \033[0;38;2;181;48;48;40m▟▀▀▙\033[0m",
            "\033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;209;35;35;40m \033[0;37;40m    \033[0;38;2;209;35;35;40m \033[0;37;40m      \033[0;38;2;209;35;35;40m   \033[0;38;2;181;48;48;40m▌\033[0;37;40m  \033[0;38;2;181;48;48;40m▐\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m      \033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m     \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;181;48;48;40m██▛▜\033[0;38;2;215;66;66;40m█\033[0;38;2;181;48;48;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m  \033[0;37;40m \033[0;38;2;209;35;35;40m   \033[0;37;40m   \033[0;38;2;209;35;35;40m \033[0;37;40m     \033[0;38;2;209;35;35;40m \033[0;37;40m  \033[0;38;2;181;48;48;40m██\033[0;38;2;209;35;35;40m \033[0;38;2;181;48;48;40m \033[0;38;2;195;50;50;40m█\033[0;38;2;181;48;48;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;209;35;35;40m      \033[0;37;40m   \033[0;38;2;209;35;35;40m    \033[0;37;40m   \033[0;38;2;209;35;35;40m  \033[0;38;2;181;48;48;40m███\033[0;38;2;143;25;25;40m███\033[0m"
    };

    @Override
    public String[] banner() { return ART; }

    @Override
    public String title() {
        return "\uD83E\uDE78 PACT - Register";
    }

    @Override
    public String[] fields() {
        return new String[]{"✎ Nome", "ꄗSenha", "Email"};
    }

    @Override
    public int[] passwordFields() {
        return new int[]{1};
    }

    @Override
    public int[] fieldLimits() {
        return new int[]{ 20, 12 }; // limite pro texto e pra senha
    }

    @Override
    public void submit(String[] values, Input input) {
        String username = values[0];

        String password = values[1];
        String email = values[2];


        // Lógica de auth vai ficar aqui dai
    }
}
