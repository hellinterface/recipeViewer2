package com.rvteam.recipeviewer2.data;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class IngredientRecipeLinkRepository implements IRepository {
    private static final String tableName = "IngredientRecipeLinks";
    private static final Class<IngredientRecipeLink> targetEntityClass = IngredientRecipeLink.class;
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
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, recipe_id INTEGER, ingredient_id INTEGER, weight REAL, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }


    private List<IngredientRecipeLink> _select(String query) {
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<IngredientRecipeLink> returnValue = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                int n_id = rs.getInt("id");
                int recipe_id = rs.getInt("recipe_id");
                int ingredient_id = rs.getInt("ingredient_id");
                float weight = rs.getFloat("weight");
                IngredientRecipeLink newLink = new IngredientRecipeLink(n_id, recipe_id, ingredient_id, weight);
                returnValue.add(newLink);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }


    @Override
    public List<IngredientRecipeLink> selectAll() {
        return _select("");
    }

    @Override
    public IngredientRecipeLink selectByID(int id) {
        String query = "WHERE id="+id;
        List<IngredientRecipeLink> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }
    public List<IngredientRecipeLink> selectByRecipeID(int id) {
        return _select("WHERE recipe_id="+id);
    }
    public List<IngredientRecipeLink> selectByIngredientID(int id) {
        return _select("WHERE ingredient_id="+id);
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        IngredientRecipeLink link = (IngredientRecipeLink)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET recipe_id=?, ingredient_id=?, weight=? WHERE id=?", tableName));
            statement.setInt(1, link.getRecipeID());
            statement.setInt(2, link.getIngredientID());
            statement.setFloat(3, link.getWeight());
            statement.setInt(4, link.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        IngredientRecipeLink link = (IngredientRecipeLink)_entity;
        Connection conn = getConnection();
        int n_id = -1;
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, recipe_id, ingredient_id, weight) VALUES (NULL, ?, ?, ?)", tableName));
            statement.setInt(1, link.getRecipeID());
            statement.setInt(2, link.getIngredientID());
            statement.setFloat(3, link.getWeight());
            statement.executeUpdate();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement2.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE recipe_id={1}, ingredient_id={2}, weight={3}", tableName, link.getRecipeID(), link.getIngredientID(), link.getWeight()));
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        link.setID(n_id);
        return n_id;
    }

    public boolean remove(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Step step = (Step)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("DELETE FROM {0} WHERE id=?", tableName));
            statement.setInt(1, step.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static IngredientRecipeLinkRepository instance;
    private IngredientRecipeLinkRepository() { }
    public static IngredientRecipeLinkRepository getInstance() {
        if (instance == null) {
            instance = new IngredientRecipeLinkRepository();
        }
        return instance;
    }
}