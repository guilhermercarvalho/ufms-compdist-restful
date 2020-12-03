package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String JDBCURL = "jdbc:postgresql://db:5432/postgres";
    private static final String USER = "postgres";
    private static final String PWD = "postgres";

    protected Connection connection;

    public Connection getConnection() {
        try {
           return connection = DriverManager.getConnection(JDBCURL, USER, PWD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
