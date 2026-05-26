package cli.core;
//IMPRIME COMPONENTES VISUAIS HEADER BOX E ERROS

public final class Screen {
    private static final int width = 42;
    private static final String border = "═".repeat(width);

    public static void clearScreen(){
        System.out.print(Ansi.CLEAR);
        System.out.flush();
    }

    public static void header(String titulo){
        System.out.println(Ansi.RED_PASTEL + "╔" + border + "╗");
        System.out.println("║" + Ansi.pastel(titulo) + "║");
        System.out.println("╚" + border + "╝" + Ansi.RESET);


    }
}
