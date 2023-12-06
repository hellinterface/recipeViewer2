package com.rvteam.recipeviewer2.data;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements IRepository {
    private static final String tableName = "Categories";
    private static final Class<Category> targetEntityClass = Category.class;
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
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, title STRING, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }

    private List<Category> _select(String query) {
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<Category> returnValue = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                Category newCategory = new Category(
                        rs.getInt("id"),
                        rs.getString("title")
                );
                returnValue.add(newCategory);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }


    @Override
    public List<Category> selectAll() {
        return _select("");
    }

    @Override
    public Category selectByID(int id) {
        String query = "WHERE id="+id;
        List<Category> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }

    public Category selectByTitle(String title) {
        String query = MessageFormat.format("WHERE title=\"{0}\"", title);
        List<Category> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Category category = (Category)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET bytes=? WHERE id=?", tableName));
            statement.setString(1, category.getTitle());
            statement.setInt(2, category.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Category category = (Category)_entity;
        Connection conn = getConnection();
        int n_id = -1;
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, title) VALUES (NULL, ?)", tableName));
            statement.setString(1, category.getTitle());
            statement.executeUpdate();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement2.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE title={1}", tableName, category.getTitle()));
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        category.setID(n_id);
        return n_id;
    }

    public boolean remove(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Category category = (Category)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("DELETE FROM {0} WHERE id=?", tableName));
            statement.setInt(1, category.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static CategoryRepository instance;
    private CategoryRepository() { }
    public static CategoryRepository getInstance() {
        if (instance == null) {
            instance = new CategoryRepository();
        }
        return instance;
    }
}
