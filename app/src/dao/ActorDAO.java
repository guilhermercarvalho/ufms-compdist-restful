package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Actor;

public class ActorDAO {
    private Connection connection;

    public ActorDAO() {
        this.connection = new DatabaseConnection().getConnection();
    }

    public void insert(Actor actor) throws SQLException {
        String sql = "INSERT INTO actor (id, name, birth_date) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, actor.getId());
            stmt.setString(2, actor.getName());
            stmt.setDate(3, actor.getBirth_date());

            stmt.execute();

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }
    }

    public void update(Actor actor) {
        String sql = "UPDATE actor SET name=?, birth_date=? WHERE id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, actor.getName());
            stmt.setDate(2, actor.getBirth_date());
            stmt.setInt(3, actor.getId());

            stmt.execute();

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Actor actor) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM actor WHERE id=?");

            stmt.setInt(1, actor.getId());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Actor getOne(int id) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM actor WHERE id=?");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            Actor actor = new Actor();
            while(result.next()) {                
                actor.setId(result.getInt("id"));
                actor.setName(result.getString("name"));
                actor.setBirth_date(result.getDate("birth_date"));
            }
                
            result.close();
            stmt.close();
            return actor;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Actor> getAll() {
        try {
            List<Actor> actors = new ArrayList<Actor>();
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM actor");
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Actor actor = new Actor();

                actor.setId(result.getInt("id"));
                actor.setName(result.getString("name"));
                actor.setBirth_date(result.getDate("birth_date"));

                actors.add(actor);
            }

            result.close();
            stmt.close();
            return actors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Actor> getCustom(String name, Date IBirth_date, Date FBirth_date) {
        try {
            List<Actor> actors = new ArrayList<Actor>();
            String sql = "SELECT * FROM actor WHERE LIKE ? AND birth_date BETWEEN ? AND ?";

            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setDate(2, IBirth_date);
            stmt.setDate(3, FBirth_date);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Actor actor = new Actor();

                actor.setId(result.getInt("id"));
                actor.setName(result.getString("name"));
                actor.setBirth_date(result.getDate("birth_date"));

                actors.add(actor);
            }

            result.close();
            stmt.close();
            return actors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
