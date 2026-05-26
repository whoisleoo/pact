package cli.core;


public final class MenuRunner {
    private final Input input;

    public MenuRunner(Input input) {
        this.input = input;
    }

    public void execute(Menu menu){
        while(true){
            String[] options = menu.options();
            Screen.clearScreen();
            if(menu.banner() != null) Screen.banner(menu.banner());
            Screen.header(menu.title());
            System.out.println();
            Screen.menuOptions(options);

            int escolha = input.readOption(options.length);
            if(escolha == 0){
                return;
            }

            Screen.clearScreen();
            menu.execute(escolha, input);
            input.pause();
        }
    }
}

