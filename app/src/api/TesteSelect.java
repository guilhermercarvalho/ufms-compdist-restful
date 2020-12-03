package api;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            
            String query = "SELECT * FROM actor WHERE name LIKE ? and birth_date=?";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            
            // Exec query and store result
            stmt.setString(1, "A%");
            stmt.setDate(2, Date.valueOf("1974-09-11"));

            ResultSet result = stmt.executeQuery();
            
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
