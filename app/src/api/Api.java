package api;

import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;

import java.util.List;
import java.util.Iterator;

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

import dao.*;
import model.*;

public class Api {

    public String getActors(String format) {
        ActorDAO actorDao = new ActorDAO();
        List<Actor> actors = actorDao.getAll();

        if (format == "json")
            return actorDao.getActorsJSON(actors);
        else
            return actorDao.getActorsXML(actors);
    }

    public String getActors(int id, String format) {
        ActorDAO actorDao = new ActorDAO();
        Actor actor = actorDao.getOne(id);

        if (format == "json")
            return actorDao.getActorJSON(actor);
        else
            return actorDao.getActorXML(actor);
    }

    public String deleteActor(int id, String format) {
        ActorDAO actorDao = new ActorDAO();
        Actor actor = actorDao.getOne(id);

        String result;

        if (format == "json")
            result = actorDao.getActorJSON(actor);
        else
            result = actorDao.getActorXML(actor);

        actorDao.remove(actor);

        return result;
    }

    public String insertActor(String body, String format) throws SQLException {
        Actor actor = new Actor();

        JSONObject my_obj = new JSONObject(body);
        Iterator<String> keys = my_obj.keys();

        int id_actor = Integer.MAX_VALUE;
        String name_actor = null;
        Date birth_date_actor = null;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.equalsIgnoreCase("id")) {
                id_actor = Integer.parseInt(my_obj.get(key).toString());
                actor.setId(id_actor);

            } else if (key.equalsIgnoreCase("name")) {
                name_actor = my_obj.get(key).toString();

            } else if (key.equalsIgnoreCase("birth_date")) {
                birth_date_actor = Date.valueOf(my_obj.get(key).toString());
            }
        }
        actor.setName(name_actor != null ? name_actor : null);
        actor.setBirth_date(birth_date_actor != null ? birth_date_actor : null);

        ActorDAO actorDAO = new ActorDAO();
        actorDAO.insert(actor);

        actorDAO = null;
        actor = null;
        my_obj = null;
        keys = null;

        return getActors(id_actor, format);
    }

    public String updateActor(int id, String body, String format) throws SQLException {
        ActorDAO actorDao = new ActorDAO();
        Actor actor = actorDao.getOne(id);

        JSONObject my_obj = new JSONObject(body);
        Iterator<String> keys = my_obj.keys();

        String name_actor = null;
        Date birth_date_actor = null;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.equalsIgnoreCase("name")) {
                name_actor = my_obj.get(key).toString();
            } else if (key.equalsIgnoreCase("birth_date")) {
                birth_date_actor = Date.valueOf(my_obj.get(key).toString());
            }
        }

        actor.setName(name_actor != null ? name_actor : actor.getName());
        actor.setBirth_date(birth_date_actor != null ? birth_date_actor : actor.getBirth_date());

        actorDao.update(actor);

        actorDao = null;
        actor = null;
        my_obj = null;
        keys = null;

        return getActors(id, format);
    }

    public String searchActor(String body, String format) {
        ActorDAO actorDao = new ActorDAO();

        JSONObject my_obj = new JSONObject(body);
        Iterator<String> keys = my_obj.keys();

        String name_actor = null;
        Date birth_date_actor = null;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.equalsIgnoreCase("name")) {
                name_actor = my_obj.get(key).toString();
            } else if (key.equalsIgnoreCase("birth_date")) {
                birth_date_actor = Date.valueOf(my_obj.get(key).toString());
            }
        }

        String search_name = name_actor != null ? "%" + name_actor + "%" : "%";
        Date search_birth_date = birth_date_actor != null ? birth_date_actor : null;

        Date IBirth_date;
        Date FBirth_date;

        if (search_birth_date == null) {
            IBirth_date = Date.valueOf("1000-01-01");
            FBirth_date = Date.valueOf("9999-12-31");
        } else {
            IBirth_date = search_birth_date;
            FBirth_date = search_birth_date;
        }

        List<Actor> actors = actorDao.getCustom(search_name, IBirth_date, FBirth_date);

        if (format == "json")
            return actorDao.getActorsJSON(actors);
        else
            return actorDao.getActorsXML(actors);
    }

    public String getMovies(String format) {
        MovieDAO movieDao = new MovieDAO();
        List<Movie> movies = movieDao.getAll();

        if (format == "json")
            return movieDao.getMoviesJSON(movies);
        else
            return movieDao.getMoviesXML(movies);
    }

    public String getMovies(int id, String format) {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);

        if (format == "json")
            return movieDao.getMovieJSON(movie);
        else
            return movieDao.getMovieXML(movie);
    }

    public String deleteMovie(int id, String format) {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);

        String result;
        if (format == "json")
            result = movieDao.getMovieJSON(movie);
        else
            result = movieDao.getMovieXML(movie);

        movieDao.remove(movie);

        return result;
    }

    public String insertMovie(String body, String format) throws SQLException {
        Movie movie = new Movie();

        JSONObject my_obj = new JSONObject(body);
        Iterator<String> keys = my_obj.keys();

        int id_movie = Integer.MAX_VALUE;
        String title_movie = null;
        String synopsis_movie = null;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.equalsIgnoreCase("id")) {
                id_movie = Integer.parseInt(my_obj.get(key).toString());
                movie.setId(id_movie);

            } else if (key.equalsIgnoreCase("title")) {
                title_movie = my_obj.get(key).toString();

            } else if (key.equalsIgnoreCase("synopsis")) {
                synopsis_movie = my_obj.get(key).toString();
            }
        }

        movie.setTitle(title_movie != null ? title_movie : null);
        movie.setSynopsis(synopsis_movie != null ? synopsis_movie : null);

        MovieDAO movieDao = new MovieDAO();
        movieDao.insert(movie);

        movieDao = null;
        movie = null;
        my_obj = null;
        keys = null;

        return getMovies(id_movie, format);
    }

    public String updateMovie(int id, String body, String format) throws SQLException {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);

        JSONObject my_obj = new JSONObject(body);
        Iterator<String> keys = my_obj.keys();

        String title_movie = null;
        String synopsis_movie = null;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.equalsIgnoreCase("title")) {
                title_movie = my_obj.get(key).toString();

            } else if (key.equalsIgnoreCase("synopsis")) {
                synopsis_movie = my_obj.get(key).toString();
            }
        }

        movie.setTitle(title_movie != null ? title_movie : movie.getTitle());
        movie.setSynopsis(synopsis_movie != null ? synopsis_movie : movie.getSynopsis());

        movieDao.update(movie);

        movieDao = null;
        movie = null;
        my_obj = null;
        keys = null;

        return getMovies(id, format);
    }

    public String searchMovie(String body, String format) {
        MovieDAO movieDao = new MovieDAO();

        JSONObject my_obj = new JSONObject(body);
        Iterator<String> keys = my_obj.keys();

        String title_movie = null;
        String synopsis_movie = null;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.equalsIgnoreCase("title")) {
                title_movie = my_obj.get(key).toString();
            } else if (key.equalsIgnoreCase("synopsis")) {
                synopsis_movie = my_obj.get(key).toString();
            }
        }

        String search_title = title_movie != null ? "%" + title_movie + "%" : "%";
        String search_synopsis = synopsis_movie != null ? "%" + synopsis_movie + "%" : "%";

        List<Movie> movies = movieDao.getCustom(search_title, search_synopsis);

        if (format == "json")
            return movieDao.getMoviesJSON(movies);
        else
            return movieDao.getMoviesXML(movies);
    }

    public String getMovieActors(int id, String format) {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);
        MovieActorDAO maDao = new MovieActorDAO();
        ActorDAO actorDao = new ActorDAO();
        List<MovieActor> mas = maDao.searchMovieActors(id);
        try {
            if (format == "json") {
                JSONObject movies = new JSONObject();
                JSONArray results_movie = new JSONArray();
                JSONArray results_actors = new JSONArray();
                JSONObject result_final = new JSONObject();

                movies.put("id", movie.getId());
                movies.put("title", movie.getTitle());
                movies.put("synopsis", movie.getSynopsis());

                for (MovieActor ma : mas) {
                    JSONObject actors = new JSONObject();
                    Actor actor = actorDao.getOne(ma.getActorid());

                    actors.put("id", actor.getId());
                    actors.put("name", actor.getName());
                    actors.put("birth_date", actor.getBirth_date());

                    results_actors.put(actors);
                }
                movies.put("actors", results_actors);
                results_movie.put(movies);
                result_final.put("results", results_movie);

                return result_final.toString();
            } else {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElement("movie_actors");
                doc.appendChild(rootElement);

                Element el_movie = doc.createElement("movie");
                rootElement.appendChild(el_movie);

                Attr attr_id = doc.createAttribute("id");
                attr_id.setValue(String.valueOf(movie.getId()));
                el_movie.setAttributeNode(attr_id);

                Element el_title = doc.createElement("title");
                el_title.appendChild(doc.createTextNode(movie.getTitle() != null ? movie.getTitle() : "null"));
                el_movie.appendChild(el_title);

                Element el_synopsis = doc.createElement("synopsis");
                el_synopsis.appendChild(
                        doc.createTextNode(movie.getSynopsis() != null ? movie.getSynopsis().toString() : "null"));
                el_movie.appendChild(el_synopsis);

                Element el_actors = doc.createElement("actors");
                el_movie.appendChild(el_actors);

                for (MovieActor ma : mas) {
                    Actor actor = actorDao.getOne(ma.getActorid());

                    Element el_actor = doc.createElement("actor");
                    el_actors.appendChild(el_actor);

                    Attr id_actor = doc.createAttribute("id");
                    id_actor.setValue(String.valueOf(actor.getId()));
                    el_actor.setAttributeNode(id_actor);

                    Element el_name = doc.createElement("name");
                    el_name.appendChild(doc.createTextNode(actor.getName() != null ? actor.getName() : "null"));
                    el_actor.appendChild(el_name);

                    Element el_birth_date = doc.createElement("birth_date");
                    el_birth_date.appendChild(doc
                            .createTextNode(actor.getBirth_date() != null ? actor.getBirth_date().toString() : "null"));
                    el_actor.appendChild(el_birth_date);
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StringWriter writer = new StringWriter();

                transformer.transform(source, new StreamResult(writer));

                return writer.getBuffer().toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
