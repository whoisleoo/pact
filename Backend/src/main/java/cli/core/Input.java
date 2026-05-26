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
     System.out.print(" " + placeholder + ": ");
     return scanner.nextLine().trim();
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

public void pause(){
     System.out.print(Ansi.DIM + "\n Pressione Enter para continuar..." + Ansi.RESET);
     scanner.nextLine();
}

}
