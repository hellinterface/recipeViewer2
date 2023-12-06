package com.rvteam.recipeviewer2.data;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository implements IRepository {
    private static final String tableName = "Ingredients";
    private static final Class<Ingredient> targetEntityClass = Ingredient.class;
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
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, name STRING, calories REAL, bzu_b REAL, bzu_z REAL, bzu_u REAL, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }


    private List<Ingredient> _select(String query) {
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<Ingredient> returnValue = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                Ingredient newIngredient = new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("calories"),
                        rs.getFloat("bzu_b"),
                        rs.getFloat("bzu_z"),
                        rs.getFloat("bzu_u")
                );
                returnValue.add(newIngredient);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }


    @Override
    public List<Ingredient> selectAll() {
        return _select("");
    }

    @Override
    public Ingredient selectByID(int id) {
        String query = "WHERE id="+id;
        List<Ingredient> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }

    public Ingredient selectByName(String name) {
        String query = MessageFormat.format("WHERE name=\"{0}\"", name);
        List<Ingredient> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Ingredient ingredient = (Ingredient)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET name=?, calories=?, bzu_b=?, bzu_z=?, bzu_u=? WHERE id=?", tableName));
            statement.setString(1, ingredient.getName());
            statement.setFloat(2, ingredient.getCalories());
            statement.setFloat(3, ingredient.getBzuB());
            statement.setFloat(4, ingredient.getBzuZ());
            statement.setFloat(5, ingredient.getBzuU());
            statement.setInt(6, ingredient.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Ingredient ingredient = (Ingredient)_entity;
        Connection conn = getConnection();
        int n_id = -1;
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, name, calories, bzu_b, bzu_z, bzu_u) VALUES (NULL, ?, ?, ?, ?, ?)", tableName));
            statement.setString(1, ingredient.getName());
            statement.setFloat(2, ingredient.getCalories());
            statement.setFloat(3, ingredient.getBzuB());
            statement.setFloat(4, ingredient.getBzuZ());
            statement.setFloat(5, ingredient.getBzuU());
            statement.executeUpdate();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement2.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE name={1}, calories={2}, bzu_b={3}, bzu_z={4}, bzu_u={5}", tableName, ingredient.getName(), ingredient.getCalories(), ingredient.getBzuB(), ingredient.getBzuZ(), ingredient.getBzuU()));
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return n_id;
    }

    public boolean remove(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Ingredient ingredient = (Ingredient)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("DELETE FROM {0} WHERE id=?", tableName));
            statement.setInt(1, ingredient.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static IngredientRepository instance;
    private IngredientRepository() { }
    public static IngredientRepository getInstance() {
        if (instance == null) {
            instance = new IngredientRepository();
        }
        return instance;
    }
}
