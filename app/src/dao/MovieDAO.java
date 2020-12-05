package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Movie;

public class MovieDAO {
    private Connection connection;

    public MovieDAO() {
        this.connection = new DatabaseConnection().getConnection();
    }

    public void insert(Movie movie) throws SQLException {
        String sql = "INSERT INTO movie (id, title, synopsis) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, movie.getId());
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getSynopsis());

            stmt.execute();

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }
    }

    public void update(Movie movie) {
        String sql = "UPDATE movie SET title=?, synopsis=? WHERE id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getSynopsis());
            stmt.setInt(3, movie.getId());

            stmt.execute();

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Movie movie) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM movie WHERE id=?");

            stmt.setInt(1, movie.getId());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Movie getOne(int id) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM movie WHERE id=?");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            Movie movie = new Movie();
            while(result.next()) {                
                movie.setId(result.getInt("id"));
                movie.setTitle(result.getString("title"));
                movie.setSynopsis(result.getString("synopsis"));
            }
                
            result.close();
            stmt.close();
            return movie;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> getAll() {
        try {
            List<Movie> movies = new ArrayList<Movie>();
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM movie");
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Movie movie = new Movie();

                movie.setId(result.getInt("id"));
                movie.setTitle(result.getString("title"));
                movie.setSynopsis(result.getString("synopsis"));

                movies.add(movie);
            }

            result.close();
            stmt.close();

            return movies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> getCustom(String title, String synopsis) {
        try {
            List<Movie> movies = new ArrayList<Movie>();
            String sql = "SELECT * FROM movie WHERE title LIKE ? AND synopsis LIKE ?";

            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, title);
            stmt.setString(2, synopsis);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Movie movie = new Movie();

                movie.setId(result.getInt("id"));
                movie.setTitle(result.getString("title"));
                movie.setSynopsis(result.getString("synopsis"));

                movies.add(movie);
            }

            result.close();
            stmt.close();

            return movies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
