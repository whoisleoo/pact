package cli.menus;

import cli.core.Input;
import cli.core.Menu;
import cli.core.MenuRunner;
import cli.core.Screen;

public class TesteMenu implements Menu {
    private final MenuRunner runner;

    public TesteMenu(MenuRunner runner) {
        this.runner = runner;
    }

    @Override
    public String title() {
        return "Home";
    }

    @Override
    public String[] options() {
        return new String[]{"Opção A", "Opção B"};
    }

    @Override
    public void execute(int escolha, Input input) {
        switch (escolha) {
            case 1 -> Screen.success("Opção A escolhida!");
            case 2 -> Screen.success("Opção B escolhida!");
        }
    }
}