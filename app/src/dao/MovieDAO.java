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

    public List<Movie> getCustom(List<String> query) {
        try {
            List<Movie> movies = new ArrayList<Movie>();
            String sql = "SELECT * FROM movie WHERE";

            int iTitle = query.indexOf("title");
            int iSynopsis = query.indexOf("synopsis");

            boolean bTitle = false;
            boolean bSynopsis = false;

            if (iTitle == -1 && iSynopsis == -1)
                throw new RuntimeException("Invalid query!");

            if (iTitle != -1) {
                bTitle = true;
                if (query.get(iTitle) == "null")
                    sql += " title IS ? ";
                else
                    sql += " title LIKE ? ";
            }

            if (iTitle != -1 && iSynopsis != -1)
                sql += " AND ";

            if (iSynopsis != -1) {
                bSynopsis = true;
                sql += " synopsis=? ";
            }

            PreparedStatement stmt = this.connection.prepareStatement(sql);

            if (bTitle && bSynopsis) {
                stmt.setString(1, query.get(iTitle));
                stmt.setString(2, query.get(iSynopsis));

            } else if (bTitle && !bSynopsis) {
                stmt.setString(1, query.get(iTitle));
            } else {
                stmt.setString(1, query.get(iSynopsis));
            }

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
