package com.rvteam.recipeviewer2.data;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class StepRepository implements IRepository {
    private static final String tableName = "Steps";
    private static final Class<Step> targetEntityClass = Step.class;
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
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, recipe_id INTEGER, text STRING, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }


    private List<Step> _select(String query) {
        PhotoRepository photoRepository = PhotoRepository.getInstance();
        StepPhotoLinkRepository stepPhotoLinkRepository = StepPhotoLinkRepository.getInstance();
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<Step> returnValue = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                int n_id = rs.getInt("id");
                String n_text = rs.getString("text");
                int n_recipeId = rs.getInt("recipe_id");
                List<Photo> n_photos = new ArrayList<Photo>();
                List<StepPhotoLink> links = stepPhotoLinkRepository.selectByStepID(n_id);
                for (var i: links) {
                    Photo photo = photoRepository.selectByID(i.getPhotoID());
                    n_photos.add(photo);
                }
                Step newStep = new Step(n_id, n_recipeId, n_text, n_photos);
                returnValue.add(newStep);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }


    @Override
    public List<Step> selectAll() {
        return _select("");
    }

    @Override
    public Step selectByID(int id) {
        String query = "WHERE id="+id;
        List<Step> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }

    public List<Step> selectByRecipeID(int id) {
        return _select("WHERE recipe_id="+id);
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Step step = (Step)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET text=? WHERE id=?", tableName));
            statement.setString(1, step.getText());
            statement.setInt(6, step.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Step step = (Step)_entity;
        Connection conn = getConnection();
        PhotoRepository photoRepository = PhotoRepository.getInstance();
        StepPhotoLinkRepository stepPhotoLinkRepository = StepPhotoLinkRepository.getInstance();
        int n_id = -1;
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, recipe_id, text) VALUES (NULL, ?, ?)", tableName));
            statement.setInt(1, step.getRecipeID());
            statement.setString(2, step.getText());
            statement.executeUpdate();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement2.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE text=\"{1}\"", tableName, step.getText()));
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
            for (var i : step.getPhotos()) {
                if (photoRepository.selectByID(i.getID()) == null) {
                    photoRepository.insert(i);
                    StepPhotoLink link = new StepPhotoLink(-1, n_id, i.getID());
                    stepPhotoLinkRepository.insert(link);
                }
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        step.setID(n_id);
        return n_id;
    }

    public boolean remove(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Step step = (Step)_entity;
        Connection conn = getConnection();
        StepPhotoLinkRepository stepPhotoLinkRepository = StepPhotoLinkRepository.getInstance();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("DELETE FROM {0} WHERE id=?", tableName));
            statement.setInt(1, step.getID());
            statement.executeUpdate();

            List<StepPhotoLink> links = stepPhotoLinkRepository.selectByStepID(step.getID());
            for (var i: links) {
                stepPhotoLinkRepository.remove(i);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static StepRepository instance;
    private StepRepository() { }
    public static StepRepository getInstance() {
        if (instance == null) {
            instance = new StepRepository();
        }
        return instance;
    }
}
