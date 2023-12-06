package com.rvteam.recipeviewer2.data;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class PhotoRepository implements IRepository {
    private static final String tableName = "Photos";
    private static final Class<Photo> targetEntityClass = Photo.class;
    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:mainDatabase.db");
        }
        catch(SQLException e) { System.out.println("SQLEXCEPTION"); }
        return connection;
    }
    @Override
    public boolean createTable() {
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, bytes BLOB, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }

    private List<Photo> _select(String query) {
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<Photo> returnValue = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                Photo newPhoto = new Photo(
                        rs.getInt("id"),
                        rs.getBytes("bytes")
                );
                returnValue.add(newPhoto);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }

    @Override
    public List<Photo> selectAll() {
        return _select("");
    }

    @Override
    public Photo selectByID(int id) {
        String query = "WHERE id="+id;
        List<Photo> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Photo photo = (Photo)_entity;
        Connection conn = getConnection();
        try {
            SerialBlob photoBlob = new SerialBlob(photo.getBytes());
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET bytes=? WHERE id=?", tableName));
            statement.setBlob(1, photoBlob);
            statement.setInt(2, photo.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _photo) {
        if (_photo.getClass() != Photo.class) {
            throw new ClassCastException("Expected Photo, got " + _photo.getClass().getName());
        }
        Photo photo = (Photo)_photo;
        Connection conn = getConnection();
        int n_id = -1;
        try {
            SerialBlob photoBlob = new SerialBlob(photo.getBytes());
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, bytes) VALUES (NULL, ?)", tableName));
            statement.setBlob(1, photoBlob);
            statement.executeUpdate();
            PreparedStatement statement2 = conn.prepareStatement(MessageFormat.format("SELECT * FROM {0} WHERE bytes=?", tableName));
            statement2.setBlob(1, photoBlob);
            ResultSet rs = statement2.executeQuery();
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        photo.setID(n_id);
        return n_id;
    }

    public boolean remove(IEntity _photo) {
        if (_photo.getClass() != Photo.class) {
            throw new ClassCastException("Expected Photo, got " + _photo.getClass().getName());
        }
        Photo photo = (Photo)_photo;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Photos WHERE id=?");
            statement.setInt(1, photo.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static PhotoRepository instance;
    private PhotoRepository() { }
    public static PhotoRepository getInstance() {
        if (instance == null) {
            instance = new PhotoRepository();
        }
        return instance;
    }
}
