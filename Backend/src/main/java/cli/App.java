package cli;

import cli.core.Input;
import cli.core.MenuRunner;
import cli.core.Screen;
import cli.menus.MainMenu;

import java.io.PrintStream;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        Scanner scanner = new Scanner(System.in, "UTF-8");
        Input input = new Input(scanner);
        MenuRunner runner = new MenuRunner(input);

        runner.execute(new MainMenu(runner));
        scanner.close();
    }
}
