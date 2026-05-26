package cli.core;
import java.util.Scanner;

//LE E VALIDA ENTRADA DO USUARIO

public final class Input {
 private final Scanner scanner;

 public Input(Scanner scanner){
     this.scanner = scanner;
 }

 public int readOption(int max){
     while(true){
         System.out.print(Ansi.BOLD + "\n Escolha: " + Ansi.RESET);
         try{
             int o = Integer.parseInt(scanner.nextLine().trim());
             if(o >= 0 && o <= max){
                 return o;
             }
             Screen.error("Digite um número entre 0 e " + max);
         }catch (NumberFormatException e){
             Screen.error("Entrada inválida.");
         }
     }
 }

 public String readText(String placeholder) {
     while(true) {
         System.out.print(" " + placeholder + ": ");
         String s = scanner.nextLine().trim();
         if (!s.isEmpty()) {
             return s;
         }
         Screen.error("Campo obrigatório.");
     }
 }

 public int readInt(String placeholder){
     while(true){
         System.out.print(" " + placeholder + ": ");
         try{
             return Integer.parseInt(scanner.nextLine().trim());
         }catch (NumberFormatException e){
            Screen.error("Digite um número inteiro.");
         }
     }
 }

    public Double readDouble(String placeholder){
        while(true){
            System.out.print(" " + placeholder + ": ");
            try{
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            }catch (NumberFormatException e){
                Screen.error("Digite um número inteiro.");
            }
        }
    }

public void pause(){
     System.out.print(Ansi.DIM + "\n Pressione Enter para continuar..." + Ansi.RESET);
     scanner.nextLine();
}

}
