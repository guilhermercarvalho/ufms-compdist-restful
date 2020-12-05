package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.MovieActor;

public class MovieActorDAO {
    private Connection connection;

    public MovieActorDAO() {
        this.connection = new DatabaseConnection().getConnection();
    }

    public List<MovieActor> searchMovieActors(int id) {
        try {
            List<MovieActor> mas = new ArrayList<MovieActor>();
            PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM movie_actor WHERE movieid=?"
            );
            
            stmt.setInt(1, id);
            
            ResultSet result = stmt.executeQuery();

            while(result.next()) {                
                MovieActor ma = new MovieActor();
                ma.setActorid(result.getInt("actorid"));
                
                mas.add(ma);
            }

            result.close();
            stmt.close();
            return mas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
