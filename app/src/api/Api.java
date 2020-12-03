package api;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

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
    
    public String insertActor(int id, String name, String birth_date) throws SQLException{
        Actor actor = new Actor();
        actor.setId(id);
        actor.setName(name);
        actor.setBirth_date(Date.valueOf(birth_date));
        
        ActorDAO actorDAO = new ActorDAO();
        actorDAO.insert(actor);

        return getActors(id);
    }

    public String updateActor(int id, String name, String birth_date) throws SQLException{
        ActorDAO actorDao = new ActorDAO();
        Actor actor = actorDao.getOne(id);

        actor.setName(name);
        actor.setBirth_date(Date.valueOf(birth_date));
        
        actorDao.update(actor);

        return getActors(id);
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

    public String insertMovie(int id, String title, String synopsis) throws SQLException{
        MovieDAO movieDao = new MovieDAO();
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setSynopsis(synopsis);
        
        movieDao.insert(movie);

        return getMovies(id);
    }

    public String updateMovie(int id, String title, String synopsis) throws SQLException{
        MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getOne(id);

        movie.setTitle(title);
        movie.setSynopsis(synopsis);
        
        movieDao.update(movie);

        return getMovies(id);
    }

}
