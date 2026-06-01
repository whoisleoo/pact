package cli.core;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Attributes;
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

 public String readText(String placeholder, int maxChars) {
     while (true) {
         Screen.openPrompt(Ansi.BOLD + placeholder + " ➜" + Ansi.RESET);
         try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
             Attributes saved = terminal.enterRawMode();
             StringBuilder buffer = new StringBuilder();
             try {
                 Reader reader = terminal.reader();
                 int c;
                 while ((c = reader.read()) != -1) {
                     if (c == '\r' || c == '\n') break;
                     if (c == 127 || c == '\b') {
                         if (buffer.length() > 0) {
                             buffer.deleteCharAt(buffer.length() - 1);
                             System.out.print("\b \b");
                             System.out.flush();
                         }
                     } else if (c >= 32 && buffer.length() < maxChars) {
                         buffer.append((char) c);
                         System.out.print((char) c);
                         System.out.flush();
                     }
                 }
             } finally {
                 terminal.setAttributes(saved);
             }
             Screen.closePrompt();
             String s = buffer.toString().trim();
             if (!s.isEmpty()) return s;
         } catch (IOException e) {
             Screen.closePrompt();
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


public String readPassword(String placeholder, int maxChars) {
    while (true) {
        Screen.openPrompt(Ansi.BOLD + placeholder + " ➜" + Ansi.RESET);
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            Attributes saved = terminal.enterRawMode();
            StringBuilder buffer = new StringBuilder();
            try {
                Reader reader = terminal.reader();
                int c;
                while ((c = reader.read()) != -1) {
                    if (c == '\r' || c == '\n') break;
                    if (c == 127 || c == '\b') {
                        if (buffer.length() > 0) {
                            buffer.deleteCharAt(buffer.length() - 1);
                            System.out.print("\b \b");
                            System.out.flush();
                        }
                    } else if (c >= 32 && buffer.length() < maxChars) {
                        buffer.append((char) c);
                        System.out.print('*');
                        System.out.flush();
                    }
                }
            } finally {
                terminal.setAttributes(saved);
            }
            Screen.closePrompt();
            String pwd = buffer.toString().trim();
            if (!pwd.isEmpty()) return pwd;
        } catch (IOException e) {
            Screen.closePrompt();
        }
        Screen.error("Campo obrigatório.");
    }
}

public int selectOption(String[] options) {
    int total    = options.length + 1;
    int selected = 0;
    int boxLines = total + 2;

    Screen.menuOptions(options, selected);

    try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
        Attributes saved = terminal.enterRawMode();
        try {
            Reader reader = terminal.reader();
            int c;
            while ((c = reader.read()) != -1) {
                if (c == '\r' || c == '\n') {
                    return (selected == options.length) ? 0 : selected + 1;
                }
                if (c == 27) {
                    reader.read();
                    int arrow = reader.read();
                    if (arrow == 'A')        selected = (selected - 1 + total) % total;
                    else if (arrow == 'B')   selected = (selected + 1) % total;

                    System.out.print("\033[" + boxLines + "A");
                    System.out.flush();
                    Screen.menuOptions(options, selected);
                }
            }
        } finally {
            terminal.setAttributes(saved);
        }
    } catch (IOException e) {
        // n é pra retorna nada aqui atenção obrigado pela atenção
    }
    return 0;
}

public void pause(){
     System.out.print(Ansi.BLINK + "\n Pressione Enter para continuar..." + Ansi.RESET);
     scanner.nextLine();
}

}
