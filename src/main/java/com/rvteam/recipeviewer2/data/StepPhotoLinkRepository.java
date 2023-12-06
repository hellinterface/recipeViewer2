package com.rvteam.recipeviewer2.data;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class StepPhotoLinkRepository implements IRepository {
    private static final String tableName = "StepPhotoLinks";
    private static final Class<StepPhotoLink> targetEntityClass = StepPhotoLink.class;
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
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, step_id INTEGER, photo_id INTEGER, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }


    private List<StepPhotoLink> _select(String query) {
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<StepPhotoLink> returnValue = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                int n_id = rs.getInt("id");
                int step_id = rs.getInt("step_id");
                int photo_id = rs.getInt("photo_id");
                StepPhotoLink newLink = new StepPhotoLink(n_id, step_id, photo_id);
                returnValue.add(newLink);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }


    @Override
    public List<StepPhotoLink> selectAll() {
        return _select("");
    }

    @Override
    public StepPhotoLink selectByID(int id) {
        String query = "WHERE id="+id;
        List<StepPhotoLink> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }
    public List<StepPhotoLink> selectByPhotoID(int id) {
        return _select("WHERE photo_id="+id);
    }
    public List<StepPhotoLink> selectByStepID(int id) {
        return _select("WHERE step_id="+id);
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        StepPhotoLink link = (StepPhotoLink)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET step_id=?, photo_id=? WHERE id=?", tableName));
            statement.setInt(1, link.getStepID());
            statement.setInt(2, link.getPhotoID());
            statement.setInt(3, link.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        StepPhotoLink link = (StepPhotoLink)_entity;
        Connection conn = getConnection();
        int n_id = -1;
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, step_id, photo_id) VALUES (NULL, ?, ?)", tableName));
            statement.setInt(1, link.getStepID());
            statement.setInt(2, link.getPhotoID());
            statement.executeUpdate();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement2.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE step_id={1} AND photo_id={2}", tableName, link.getStepID(), link.getPhotoID()));
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        link.setID(n_id);
        return n_id;
    }

    public Integer push(IEntity entity) {
        if (entity.getID() != -1) {
            update(entity);
            return entity.getID();
        }
        else return insert(entity);
    }

    public boolean remove(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        StepPhotoLink link = (StepPhotoLink)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("DELETE FROM {0} WHERE id=?", tableName));
            statement.setInt(1, link.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static StepPhotoLinkRepository instance;
    private StepPhotoLinkRepository() { }
    public static StepPhotoLinkRepository getInstance() {
        if (instance == null) {
            instance = new StepPhotoLinkRepository();
        }
        return instance;
    }
}