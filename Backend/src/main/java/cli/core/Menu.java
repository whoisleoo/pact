package cli.core;

public interface Menu {
    String title();
    String[] options();
    void execute(int escolha, Input input);
    default String[] banner() {
        return null;
    }
}
