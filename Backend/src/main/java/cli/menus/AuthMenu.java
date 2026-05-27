package cli.menus;

import cli.core.Input;
import cli.core.Menu;
import cli.core.MenuRunner;
import cli.core.Screen;
import cli.core.FormRunner;

public class AuthMenu implements Menu {
    private static final String[] ART = {
            "\033[0;37;40m \033[0m",
            "\033[0;37;40m    \033[0;38;2;172;32;32;40m█\033[0m",
            "\033[0;37;40m   \033[0;38;2;224;103;103;40m█\033[0;38;2;211;69;69;40m█\033[0;38;2;172;32;32;40m█\033[0m",
            "\033[0;37;40m  \033[0;38;2;224;103;103;40m█\033[0;38;2;213;83;83;40m█\033[0;38;2;211;69;69;40m█\033[0;38;2;192;53;53;40m█\033[0;38;2;172;32;32;40m█\033[0;37;40m   \033[0;97;40m ▐▛▀▜ \033[0;37;40m \033[0;97;40m ▛▀▜    ▛▀▜  ▛▀▜█▛▀▜\033[0m",
            "\033[0;37;40m \033[0;38;2;213;83;83;40m██\033[0;38;2;211;69;69;40m██\033[0;38;2;192;53;53;40m██\033[0;38;2;128;20;20;40m█\033[0;37;40m \033[0;97;40m  ▐ \033[0;37;40m \033[0;97;40m ▌ \033[0;37;40m \033[0;97;40m  ▐   ▐   \033[0;37;40m  \033[0;97;40m \033[0;37;40m \033[0;97;40m █\033[0m",
            "\033[0;37;40m \033[0;38;2;172;32;32;40m█\033[0;38;2;192;53;53;40m██\033[0;38;2;179;41;41;40m███\033[0;38;2;109;18;18;40m█\033[0;37;40m \033[0;97;40m  ▐▄▄▟   ▛▀▜\033[0;37;40m \033[0;97;40m  ▐ \033[0;37;40m      \033[0;97;40m █\033[0m",
            "\033[0;37;40m  \033[0;38;2;148;20;20;40m██\033[0;38;2;128;20;20;40m█\033[0;38;2;109;18;18;40m██\033[0;37;40m   \033[0;97;40m ▐      ▙▄▟ \033[0;37;40m  \033[0;97;40m ▙▄▟\033[0;37;40m    \033[0;97;40m █\033[0m"
    };

    private final MenuRunner runner;

    public AuthMenu(MenuRunner runner){
        this.runner = runner;
    }



    @Override
    public String[] banner() { return ART; }

    @Override
    public String title() {
        return "\uD83E\uDE78 PACT - Auth!";
    }

    @Override
    public String[] options() {
        return new String[]{"Login", "Registro"};
    }

    @Override
    public void execute(int escolha, Input input) {
        switch (escolha) {
            case 1 -> new FormRunner(input).execute(new LoginMenu());
            case 2 -> runner.execute(new RegisterMenu(runner));

        }
    }

    @Override
    public String note(){
        return "Seja bem vindo ao PACT!";
    }


}

