package com.rvteam.recipeviewer2.data;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class CartItemRepository implements IRepository {
    private static final String tableName = "CartItems";
    private static final Class<CartItem> targetEntityClass = CartItem.class;
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
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, ingredient_id INTEGER, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }


    private List<CartItem> _select(String query) {
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<CartItem> returnValue = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                int n_id = rs.getInt("id");
                int ingredient_id = rs.getInt("ingredient_id");
                CartItem newItem = new CartItem(n_id, ingredient_id);
                returnValue.add(newItem);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }


    @Override
    public List<CartItem> selectAll() {
        return _select("");
    }

    @Override
    public CartItem selectByID(int id) {
        String query = "WHERE id="+id;
        List<CartItem> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }
    public List<CartItem> selectByIngredientID(int id) {
        return _select("WHERE ingredient_id="+id);
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        CartItem item = (CartItem)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET ingredient_id=? WHERE id=?", tableName));
            statement.setInt(1, item.getIngredientID());
            statement.setInt(2, item.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        CartItem item = (CartItem)_entity;
        Connection conn = getConnection();
        int n_id = -1;
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, ingredient_id) VALUES (NULL, ?)", tableName));
            statement.setInt(1, item.getIngredientID());
            statement.executeUpdate();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement2.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE ingredient_id={1}", tableName, item.getIngredientID()));
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        item.setID(n_id);
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
        CartItem item = (CartItem)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("DELETE FROM {0} WHERE id=?", tableName));
            statement.setInt(1, item.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static CartItemRepository instance;
    private CartItemRepository() { }
    public static CartItemRepository getInstance() {
        if (instance == null) {
            instance = new CartItemRepository();
        }
        return instance;
    }
}