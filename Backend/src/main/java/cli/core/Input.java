package cli.core;
import java.util.Scanner;

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
         Screen.openPrompt(placeholder + " ➜");
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

    public Double readDouble(String placeholder){
        while(true){
            Screen.openPrompt(placeholder + " ➜");
            try{
                double d = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                Screen.closePrompt();
                return d;
            }catch (NumberFormatException e){
                Screen.closePrompt();
                Screen.error("Digite um número válido.");
            }
        }
    }

public void pause(){
     System.out.print(Ansi.BLINK + "\n Pressione Enter para continuar..." + Ansi.RESET);
     scanner.nextLine();
}

}
