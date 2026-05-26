package cli.core;

/*
*   Fonte pros códigos ANSI: https://1.tools/text/ansi/
*/


public final class Ansi {
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String DIM = "\033[2m";
    public static final String BLINK = "\033[5m";
    public static final String CLEAR = "\033[H\033[2J\033[3J";

    public static final String RED = "\033[31m"; // ERROR
    public static final String GREEN = "\033[32m"; // SUCESS
    public static final String YELLOW = "\033[33m"; // WARNING

    public static final String RED_PASTEL = "\033[38;5;210m";
    public static final String WHITE_PASTEL = "\033[38;5;255m";



    public static String rgb(int r, int g, int b){
        return "\033[38;2;" + r + ";" + g + ";" + b + "m";
    }

    public static String bold(String s){
        return BOLD + s + RESET;
    }

    public static String dim(String s){
        return DIM  + s + RESET;
    }



    public static String red(String s){
        return RED + s + RESET;
    }

    public static String green(String s){
        return GREEN + s + RESET;
    }

    public static String yellow(String s){
        return YELLOW + s + RESET;
    }

    public static String pastel(String s){
        return RED_PASTEL + s + RESET;
    }

    public static String soft(String s){
        return WHITE_PASTEL + s + RESET;
    }


    private Ansi(){};
}
