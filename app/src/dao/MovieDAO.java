package dao;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
            while (result.next()) {
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

    public String getMoviesJSON(List<Movie> movies) {
        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();

        for (Movie movie : movies) {
            JSONObject my_obj = new JSONObject();
            my_obj.put("id", movie.getId());
            my_obj.put("title", movie.getTitle());
            my_obj.put("synopsis", movie.getSynopsis());
            results.put(my_obj);
        }
        result.put("results", results);

        return result.toString();
    }

    public String getMoviesXML(List<Movie> movies) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("movies");
            doc.appendChild(rootElement);
            
            for (Movie movie : movies) {
                Element el_movie = doc.createElement("movie");
                rootElement.appendChild(el_movie);

                Attr attr_id = doc.createAttribute("id");
                attr_id.setValue(String.valueOf(movie.getId()));
                el_movie.setAttributeNode(attr_id);

                Element el_title = doc.createElement("title");
                el_title.appendChild(doc.createTextNode(
                    movie.getTitle() != null ? movie.getTitle() : "null"
                ));
                el_movie.appendChild(el_title);

                Element el_synopsis = doc.createElement("synopsis");
                el_synopsis.appendChild(doc.createTextNode(
                    movie.getSynopsis() != null ? movie.getSynopsis() : "null"
                ));
                el_movie.appendChild(el_synopsis);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();

            transformer.transform(source, new StreamResult(writer));

            return writer.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getMovieJSON(Movie movie) {
        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();
        JSONObject my_obj = new JSONObject();

        my_obj.put("id", movie.getId());
        my_obj.put("title", movie.getTitle());
        my_obj.put("synopsis", movie.getSynopsis());
        results.put(my_obj);
        result.put("results", results);

        return result.toString();
    }

    public String getMovieXML(Movie movie) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("movie");
            doc.appendChild(rootElement);

            Attr attr_id = doc.createAttribute("id");
            attr_id.setValue(String.valueOf(movie.getId()));
            rootElement.setAttributeNode(attr_id);

            Element el_title = doc.createElement("title");
            el_title.appendChild(doc.createTextNode(
                movie.getTitle() != null ? movie.getTitle() : "null"
            ));
            rootElement.appendChild(el_title);

            Element el_synopsis = doc.createElement("synopsis");
            el_synopsis.appendChild(doc.createTextNode(
                movie.getSynopsis() != null ? movie.getSynopsis().toString() : "null"
            ));
            rootElement.appendChild(el_synopsis);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();

            transformer.transform(source, new StreamResult(writer));

            return writer.getBuffer().toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
