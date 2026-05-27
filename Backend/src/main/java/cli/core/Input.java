package cli.core;
import java.io.IOException;
import java.util.Scanner;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/*
*   Classe responsavel por ler as entradas do usuário
*   e validar elas bonitinho.
 */

public final class Input {
 private final Scanner scanner;

 public Input(Scanner scanner){
     this.scanner = scanner;
 }

 public int readOption(int max){
     while(true){
         System.out.println();
         Screen.openPrompt(Ansi.BOLD + "Escolha ➜" + Ansi.RESET);
         try{
             int o = Integer.parseInt(scanner.nextLine().trim());
             Screen.closePrompt();
             if(o >= 0 && o <= max){
                 return o;
             }
             Screen.error("Digite um número entre 0 e " + max);
         }catch (NumberFormatException e){
             Screen.closePrompt();
             Screen.error("Entrada inválida.");
         }
     }
 }

 public String readText(String placeholder) {
     while(true) {
         Screen.openPrompt(Ansi.BOLD + placeholder + " ➜" + Ansi.RESET);
         String s = scanner.nextLine().trim();
         Screen.closePrompt();
         if (!s.isEmpty()) {
             return s;
         }
         Screen.error("Campo obrigatório.");
     }
 }

 public int readInt(String placeholder){
     while(true){
         Screen.openPrompt(placeholder + " ➜");
         try{
             int o = Integer.parseInt(scanner.nextLine().trim());
             Screen.closePrompt();
             return o;
         }catch (NumberFormatException e){
             Screen.closePrompt();
             Screen.error("Digite um número inteiro.");
         }
     }
 }

    public Double readDouble(String placeholder) {
        while (true) {
            Screen.openPrompt(placeholder + " ➜");
            try {
                double d = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                Screen.closePrompt();
                return d;
            } catch (NumberFormatException e) {
                Screen.closePrompt();
                Screen.error("Digite um número válido.");
            }
        }
    }


public String readPassword(String placeholder) {
    while (true) {
        Screen.openPrompt(Ansi.BOLD + placeholder + " ➜" + Ansi.RESET);
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
            String pwd = reader.readLine('*').trim();
            Screen.closePrompt();
            if (!pwd.isEmpty()) return pwd;
        } catch (IOException e) {
            Screen.closePrompt();
        }
        Screen.error("Campo obrigatório.");
    }
}

public void pause(){
     System.out.print(Ansi.BLINK + "\n Pressione Enter para continuar..." + Ansi.RESET);
     scanner.nextLine();
}

}
