package cli.core;


public final class MenuRunner {
    private final Input input;

    public MenuRunner(Input input) {
        this.input = input;
    }

    public void execute(Menu menu){
        while (true) {
            String[] options = menu.options();
            Screen.clearScreen();
            if (menu.banner() != null) Screen.banner(menu.banner());
            System.out.println();
            System.out.println();
            Screen.header(menu.title());
            System.out.println();
            if(menu.note() != null) Screen.note(menu.note());



            int escolha = input.selectOption(options);
            if (escolha == 0) return;

            Screen.clearScreen();
            menu.execute(escolha, input);
            input.pause();
        }
    }
}

