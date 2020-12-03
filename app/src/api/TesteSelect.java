package api;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TesteSelect {

    private static final String JDBCURL = "jdbc:postgresql://db:5432/postgres";
    private static final String USER = "postgres";
    private static final String PWD = "postgres";

    // protected Connection connection;
    // protected Query query;

    public static void main(String[] args) {
        try {
            // Connect to DB
            Connection connection = DriverManager.getConnection(JDBCURL, USER, PWD);
            
            // Create Statement
            Statement stmt = connection.createStatement();
            
            // Set query
            String query = "SELECT * FROM actor";
            
            // Exec query and store result
            ResultSet result = stmt.executeQuery(query);
            
            // Show result
            int rowCount = 0;
            while (result.next()) { // Move the cursor to the next row, return false if no more row
                int id = result.getInt("id");
                String name = result.getString("name");
                Date birth_date = result.getDate("birth_date");
                System.out.println(id + ", " + name + ", " + birth_date);
                ++rowCount;
            }
            System.out.println("Total number of records = " + rowCount);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
