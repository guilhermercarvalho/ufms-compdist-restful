package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DatabaseConnection;
import models.MovieActor;

public class MovieActorDAO {
    private Connection connection;
    
    public MovieActorDAO() {
        this.connection = new DatabaseConnection().getConnection();
    }

    public void search(MovieActor movieActor) throws SQLException {
        String sql = "SELECT FROM movie_actor WHERE movieid=? AND actorid=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, movieActor.getMovieid());
            stmt.setInt(2, movieActor.getActorid());

            stmt.execute();

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }
    }

}
