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

        for (Actor actor : actors) {
            JSONObject my_obj = new JSONObject();
            my_obj.put("id", actor.getId());
            my_obj.put("name", actor.getName());
            my_obj.put("birth_date", actor.getBirth_date());
            results.put(my_obj);
        }
        result.put("results", results);

        actorDao = null;
        actors = null;
        results = null;

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

        actorDao = null;
        actor = null;
        results = null;
        my_obj = null;

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

        actorDao = null;
        actor = null;
        results = null;
        my_obj = null;

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

        actorDAO = null;
        actor = null;
        my_obj = null;
        keys = null;

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

        actor.setName(name_actor != null ? name_actor : actor.getName());
        actor.setBirth_date(birth_date_actor != null ? birth_date_actor : actor.getBirth_date());

        actorDao.update(actor);

        actorDao = null;
        actor = null;
        my_obj = null;
        keys = null;

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

        if (search_birth_date == null) {
            IBirth_date = Date.valueOf("1000-01-01");
            FBirth_date = Date.valueOf("9999-12-31");
        } else {
            IBirth_date = search_birth_date;
            FBirth_date = search_birth_date;
        }

        List<Actor> actors = actorDao.getCustom(search_name, IBirth_date, FBirth_date);

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();

        for (Actor actor : actors) {
            JSONObject my_obj_r = new JSONObject();
            my_obj_r.put("id", actor.getId());
            my_obj_r.put("name", actor.getName());
            my_obj_r.put("birth_date", actor.getBirth_date());
            results.put(my_obj_r);
        }
        result.put("results", results);

        actorDao = null;
        actors = null;
        keys = null;
        my_obj = null;
        results = null;

        return result.toString();
    }

    public String getMovies() {
        MovieDAO movieDao = new MovieDAO();
        List<Movie> movies = movieDao.getAll();

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

        movieDao = null;
        movies = null;
        results = null;

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

        movieDao = null;
        movie = null;
        results = null;
        my_obj = null;

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

        movieDao = null;
        movie = null;
        results = null;
        my_obj = null;

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

        movieDao = null;
        movie = null;
        my_obj = null;
        keys = null;

        return getMovies(id_movie);
    }

    public String updateMovie(int id, String body) throws SQLException {
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

        return getMovies(id);
    }

    public String searchMovie(String body) {
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

        JSONArray results = new JSONArray();
        JSONObject result = new JSONObject();

        for (Movie movie : movies) {
            JSONObject my_obj_r = new JSONObject();
            my_obj_r.put("id", movie.getId());
            my_obj_r.put("title", movie.getTitle());
            my_obj_r.put("synopsis", movie.getSynopsis());
            results.put(my_obj_r);
        }
        result.put("results", results);

        movieDao = null;
        movies = null;
        my_obj = null;
        results = null;

        return result.toString();
    }

    public String getMovieActors(int id) {
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);

        JSONObject movies = new JSONObject();
        JSONArray results_movie = new JSONArray();
        JSONArray results_actors = new JSONArray();
        JSONObject result_final = new JSONObject();
        
        movies.put("id", movie.getId());
        movies.put("title", movie.getTitle());
        movies.put("synopsis", movie.getSynopsis());
        
        MovieActorDAO maDao = new MovieActorDAO();
        List<MovieActor> mas = maDao.searchMovieActors(id);
        
        ActorDAO actorDao = new ActorDAO();
        
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
        
        movieDao = null;
        movie = null;
        movies = null;
        results_movie = null;

        maDao = null;
        mas = null;

        actorDao = null;
        results_actors = null;

        return result_final.toString();
    }

}
