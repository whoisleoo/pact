package cli.core;
/*
*  Função responsavel por criar os Headers e os
*  menu de opções dentro do CLI.
*/

public final class Screen {
    private static final int width = 42;
    private static final String border = "─".repeat(width);

    public static void clearScreen(){
        System.out.print(Ansi.CLEAR);
        System.out.flush();
    }

    public static void header(String titulo){
        System.out.println(Ansi.RED_PASTEL + "╭" + border + "╮");
        System.out.println("│" + center(Ansi.pastel(titulo), width) + "│");
        System.out.println("╰" + border + "╯" + Ansi.RESET);

    }

    public static void menuOptions(String[] options){
        for(int i = 0; i < options.length; i++){
            System.out.printf("%s[%d]%s %s%n", Ansi.BOLD, i + 1, Ansi.RESET, options[i]);
        }
        System.out.printf("%s[0]%s Voltar%n", Ansi.BOLD, Ansi.RESET);
    }

    public static void sucess(String msg){
        System.out.println(Ansi.green("\n ✔ " + msg));
    }

    public static void error(String msg){
        System.out.println(Ansi.red("\n × " + msg));
    }

    public static void warning(String msg){
        System.out.println(Ansi.yellow("\n ⓘ " + msg));
    }



    public static String center(String texto, int largura){
        int pad = Math.max(0, (largura - texto.length()) / 2);
        int padDireita = Math.max(0, largura - texto.length() - pad);
        return " ".repeat(pad) + texto + " ".repeat(padDireita);


    }

    private Screen(){}
}
