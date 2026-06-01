package cli.core;

public interface Form {
    String title();
    String[] fields();
    void submit(String[] values, Input input);
    default String[] banner() {
        return null;
    }
    default int[] passwordFields() {
        return new int[]{};
    }

    default int[] fieldLimits() {
        return new int[]{};
    }
}
