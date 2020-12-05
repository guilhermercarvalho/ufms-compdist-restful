package api;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONArray;

import dao.*;
import model.*;

public class Api {

    public String getActors() {
        ActorDAO actorDao = new ActorDAO();
        List<Actor> actors = actorDao.getAll();

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();
        JSONObject my_obj = new JSONObject();

        for (Actor actor : actors) {
            my_obj.put("id", actor.getId());
            my_obj.put("name", actor.getName());
            my_obj.put("birth_date", actor.getBirth_date());
            results.put(my_obj);
            result.put("results", results);
        }

        return result.toString();
    }

    public String getActors(int id) {
        ActorDAO actorDao = new ActorDAO();
        Actor actor = actorDao.getOne(id);

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();
        JSONObject my_obj = new JSONObject();

        my_obj.put("id", actor.getId());
        my_obj.put("name", actor.getName());
        my_obj.put("birth_date", actor.getBirth_date());
        results.put(my_obj);
        result.put("results", results);

        return result.toString();
    }

    public String deleteActors(int id) {
        ActorDAO actorDao = new ActorDAO();
        Actor actor = actorDao.getOne(id);

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();
        JSONObject my_obj = new JSONObject();

        my_obj.put("id", actor.getId());
        my_obj.put("name", actor.getName());
        my_obj.put("birth_date", actor.getBirth_date());
        results.put(my_obj);
        result.put("results", results);

        actorDao.remove(actor);

        return result.toString();
    }

    public String insertActor(String body) throws SQLException {
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

        return getActors(id_actor);
    }

    public String updateActor(int id, String body) throws SQLException {
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

        actor.setName(name_actor != null ? name_actor : null);
        actor.setBirth_date(birth_date_actor != null ? birth_date_actor : null);

        actorDao.update(actor);

        return getActors(id);
    }

    public String searchActor(String body) {
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
        
        if(search_birth_date == null) {
            IBirth_date = Date.valueOf("1000-01-01");
            FBirth_date = Date.valueOf("9999-12-31");
        } else {
            IBirth_date = search_birth_date;
            FBirth_date = search_birth_date;
        }
        
        List<Actor> actors = actorDao.getCustom(search_name, IBirth_date, FBirth_date);

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();
        JSONObject my_obj = new JSONObject();

        for (Actor actor : actors) {
            my_obj.put("id", actor.getId());
            my_obj.put("name", actor.getName());
            my_obj.put("birth_date", actor.getBirth_date());
            results.put(my_obj);
            result.put("results", results);
        }

        return result.toString();
    }

    public String getMovies() {
        MovieDAO movieDao = new MovieDAO();
        List<Movie> movies = movieDao.getAll();

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();
        JSONObject my_obj = new JSONObject();

        for (Movie movie : movies) {
            my_obj.put("id", movie.getId());
            my_obj.put("title", movie.getTitle());
            my_obj.put("synopsis", movie.getSynopsis());
            results.put(my_obj);
            result.put("results", results);
        }

        return result.toString();
    }

    public String getMovies(int id) {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);

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

    public String deleteMovies(int id) {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();
        JSONObject my_obj = new JSONObject();

        my_obj.put("id", movie.getId());
        my_obj.put("title", movie.getTitle());
        my_obj.put("synopsis", movie.getSynopsis());
        results.put(my_obj);
        result.put("results", results);

        movieDao.remove(movie);

        return result.toString();
    }

    public String insertMovie(String body) throws SQLException {
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

        return getMovies(id_movie);
    }

    public String updateMovie(int id, String body) throws SQLException {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);
        ;

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

        movie.setTitle(title_movie != null ? title_movie : null);
        movie.setSynopsis(synopsis_movie != null ? synopsis_movie : null);

        movieDao.update(movie);

        return getMovies(id);
    }

}
