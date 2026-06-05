package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {

    private static final String URL = env(
        "DB_URL",
        "jdbc:mysql://localhost:3306/di_foda?useSSL=false&serverTimezone=UTC"
    );
    private static final String USER = env("DB_USER", "root");
    private static final String PASSWORD = env("DB_PASSWORD", "");

    private ConnectionFactory() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String env(String chave, String padrao) {
        String valor = System.getenv(chave);
        return (valor == null) ? padrao : valor;
    }
}
