package dao;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
            while (result.next()) {
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
            String sql = "SELECT * FROM actor WHERE name LIKE ? AND birth_date BETWEEN ? AND ?";

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

    public String getActorsJSON(List<Actor> actors) {
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
        return result.toString();
    }

    public String getActorsXML(List<Actor> actors) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("actors");
            doc.appendChild(rootElement);
            
            for (Actor actor : actors) {
                Element el_actor = doc.createElement("actor");
                rootElement.appendChild(el_actor);

                Attr attr_id = doc.createAttribute("id");
                attr_id.setValue(String.valueOf(actor.getId()));
                el_actor.setAttributeNode(attr_id);

                Element el_name = doc.createElement("name");
                el_name.appendChild(doc.createTextNode(
                    actor.getName() != null ? actor.getName() : "null"
                ));
                el_actor.appendChild(el_name);

                Element el_birth_date = doc.createElement("birth_date");
                el_birth_date.appendChild(doc.createTextNode(
                    actor.getBirth_date() != null ? actor.getBirth_date().toString() : "null"
                ));
                el_actor.appendChild(el_birth_date);
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

    public String getActorJSON(Actor actor) {
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

    public String getActorXML(Actor actor) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("actor");
            doc.appendChild(rootElement);

            Attr attr_id = doc.createAttribute("id");
            attr_id.setValue(String.valueOf(actor.getId()));
            rootElement.setAttributeNode(attr_id);

            Element el_name = doc.createElement("name");
            el_name.appendChild(doc.createTextNode(
                actor.getName() != null ? actor.getName() : "null"
            ));
            rootElement.appendChild(el_name);

            Element el_birth_date = doc.createElement("birth_date");
            el_birth_date.appendChild(doc.createTextNode(
                actor.getBirth_date() != null ? actor.getBirth_date().toString() : "null"
            ));
            rootElement.appendChild(el_birth_date);

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
