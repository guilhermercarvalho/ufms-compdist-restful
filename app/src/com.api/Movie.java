import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Movie {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:postgresql://db:5432/postgres";
        String username = "postgres";
        String password = "postgres";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Conectado");
            connection.close();
        } catch (SQLException e) {
            System.err.println("Erro de conexão com PostgreSQL: " + e);
            e.printStackTrace();
        }
    }
}