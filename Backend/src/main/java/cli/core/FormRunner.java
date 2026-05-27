package cli.core;

public final class FormRunner {
    private final Input input;

    public FormRunner(Input input){
        this.input = input;
    }

    public void execute(Form form){
        Screen.clearScreen();
        if(form.banner() != null) Screen.banner(form.banner());
        Screen.header(form.title());
        System.out.println();

        String[] fields = form.fields();
        String[] values = new String[fields.length];
        int[] pwdIndexes = form.passwordFields();

        for (int i = 0; i < fields.length; i++) {
            boolean isPassword = false;
            for (int idx : pwdIndexes) if (idx == i) { isPassword = true; break; }
            values[i] = isPassword ? input.readPassword(fields[i]) : input.readText(fields[i]);
        }

        form.submit(values, input);
    }
}
