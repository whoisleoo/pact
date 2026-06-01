package cli.core;

public final class Screen {
    private static final int width = 42; // TORNAR DINAMICO
    private static final String border = "─".repeat(width);

    public static void banner(String[] linhas){
        for(String linha : linhas) System.out.println(linha);
    }

    public static void clearScreen(){
        System.out.print(Ansi.CLEAR);
        System.out.flush();
    }

    public static void header(String titulo){
        System.out.println(Ansi.RED_PASTEL + "╭" + border + "╮");
        System.out.println("│" + center(Ansi.pastel(titulo), width) + Ansi.RED_PASTEL + "│");
        System.out.println("╰" + border + "╯" + Ansi.RESET);
    }

    public static void menuOptions(String[] options, int selected){
        System.out.println(Ansi.DIM + "╭" + border + "╮" + Ansi.RESET);
        for(int i = 0; i < options.length; i++){
            boolean active = (i == selected);
            String cursor = active ? Ansi.RED_PASTEL + " ▶" + Ansi.RESET : "  ";
            String label = active ? Ansi.BOLD + options[i] + Ansi.RESET : options[i];
            System.out.println(Ansi.DIM + "│" + Ansi.RESET + padRight(cursor + " " + label, width) + Ansi.DIM + "│" + Ansi.RESET);

        }
        boolean exitActive = (selected == options.length);
        String exitCursor = exitActive ? Ansi.RED_PASTEL + " ▶" + Ansi.RESET : "  ";
        String exitLabel  = exitActive ? Ansi.BOLD + "Sair" + Ansi.RESET : "Sair";
        System.out.println(Ansi.DIM + "│" + Ansi.RESET + padRight(exitCursor + " " + exitLabel, width) + Ansi.DIM + "│" + Ansi.RESET);
        System.out.println(Ansi.DIM + "╰" + border + "╯" + Ansi.RESET);
    }
    public static void openPrompt(String label){
        int labelLen = label.replaceAll("\033\\[[\\d;]*m", "").length();
        int trailing = Math.max(0, width - 2 - labelLen - 1);
        System.out.println(Ansi.DIM + "╭" + border + "╮" + Ansi.RESET);
        System.out.print(Ansi.DIM + "│" + Ansi.RESET + "  " + label + " ");
        System.out.print("\033[s");
        System.out.println(" ".repeat(trailing) + Ansi.DIM + "│" + Ansi.RESET);
        System.out.println(Ansi.DIM + "╰" + border + "╯" + Ansi.RESET);
        System.out.print("\033[u");
        System.out.flush();
    }

    public static void closePrompt(){
        System.out.println();
        System.out.flush();
    }

    public static void success(String msg){
        msgBox(Ansi.GREEN, "✓", msg);
    }

    public static void error(String msg){
        msgBox(Ansi.RED, "×", msg);
    }

    public static void warning(String msg){
        msgBox(Ansi.YELLOW, "ⓘ", msg);
    }

    public static void note(String msg){
        msgBox(Ansi.RED_PASTEL, "✦", msg);
    }

    private static void msgBox(String color, String icon, String msg){
        String content = "  " + color + icon + Ansi.RESET + "  " + msg;
        System.out.println();
        System.out.println(color + "╭" + border + "╮" + Ansi.RESET);
        System.out.println(color + "│" + Ansi.RESET + padRight(content, width) + color + "│" + Ansi.RESET);
        System.out.println(color + "╰" + border + "╯" + Ansi.RESET);
    }

    public static String center(String texto, int largura){
        int visivel = texto.replaceAll("\033\\[[\\d;]*m", "").length();
        int pad = Math.max(0, (largura - visivel) / 2);
        int padDireita = Math.max(0, largura - visivel - pad);
        return " ".repeat(pad) + texto + " ".repeat(padDireita);
    }

    private static String padRight(String texto, int largura){
        int visivel = texto.replaceAll("\033\\[[\\d;]*m", "").length();
        return texto + " ".repeat(Math.max(0, largura - visivel));
    }

    private Screen(){}
}
