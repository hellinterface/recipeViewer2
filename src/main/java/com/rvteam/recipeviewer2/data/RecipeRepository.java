package com.rvteam.recipeviewer2.data;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepository implements IRepository {
    private static final String tableName = "Recipes";
    private static final Class<Recipe> targetEntityClass = Recipe.class;
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
            statement.executeUpdate(MessageFormat.format("CREATE TABLE IF NOT EXISTS {0} (id INTEGER, title STRING, description STRING, category_id INTEGER, mainPhoto_id INTEGER, time INTEGER, difficulty INTEGER, PRIMARY KEY('id'))", tableName));
            return true;
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return false;
    }


    private List<Recipe> _select(String query) {
        String finalQuery = MessageFormat.format("SELECT * FROM {0} {1}", tableName, query);
        ArrayList<Recipe> returnValue = new ArrayList<>();
        // Репозитории
        IngredientRepository ingredientRepository = IngredientRepository.getInstance();
        PhotoRepository photoRepository = PhotoRepository.getInstance();
        CategoryRepository categoryRepository = CategoryRepository.getInstance();
        IngredientRecipeLinkRepository ingredientRecipeLinkRepository = IngredientRecipeLinkRepository.getInstance();
        StepRepository stepRepository = StepRepository.getInstance();
        Connection conn = getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(finalQuery);
            while(rs.next()) {
                int n_id = rs.getInt("id");
                String n_title = rs.getString("title");
                String n_description = rs.getString("description");
                int n_category_id = rs.getInt("category_id");
                int n_mainPhoto_id = rs.getInt("mainPhoto_id");
                int n_time = rs.getInt("time");
                int n_difficulty = rs.getInt("difficulty");
                List<IngredientUse> n_ingredients = new ArrayList<IngredientUse>();
                List<Step> n_steps = new ArrayList<Step>();
                // Категория
                Category n_category = categoryRepository.selectByID(n_category_id);
                // Фото
                Photo n_mainPhoto = photoRepository.selectByID(n_mainPhoto_id);
                // Ингредиенты
                List<IngredientRecipeLink> links = ingredientRecipeLinkRepository.selectByRecipeID(n_id);
                for (var i: links) {
                    Ingredient ingredient = ingredientRepository.selectByID(i.getIngredientID());
                    IngredientUse newIngredientUse = new IngredientUse(
                            i.getID(),
                            ingredient,
                            i.getWeight()
                    );
                    n_ingredients.add(newIngredientUse);
                }
                // Шаги
                n_steps = stepRepository.selectByRecipeID(n_id);
                returnValue.add(
                        new Recipe(n_id, n_title, n_description, n_category, n_mainPhoto, n_time, n_difficulty, n_ingredients, n_steps)
                );
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return returnValue;
    }


    @Override
    public List<Recipe> selectAll() {
        return _select("");
    }

    @Override
    public Recipe selectByID(int id) {
        String query = "WHERE id="+id;
        List<Recipe> list = _select(query);
        if (!list.isEmpty()) return list.get(0);
        else return null;
    }

    public boolean update(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Recipe recipe = (Recipe)_entity;
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("UPDATE {0} SET title=?, description=?, category_id=?, mainPhoto_id=?, time=?, difficulty=? WHERE id=?", tableName));
            statement.setString(1, recipe.getTitle());
            statement.setString(2, recipe.getDescription());
            statement.setInt(3, recipe.getCategory().getID());
            statement.setInt(4, recipe.getPhoto().getID());
            statement.setInt(5, recipe.getTime());
            statement.setInt(6, recipe.getDifficulty());
            statement.setInt(7, recipe.getID());
            statement.executeUpdate();
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    public Integer insert(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Recipe recipe = (Recipe)_entity;
        Connection conn = getConnection();
        StepRepository stepRepository = StepRepository.getInstance();
        IngredientRecipeLinkRepository ingredientRecipeLinkRepository = IngredientRecipeLinkRepository.getInstance();
        IngredientRepository ingredientRepository = IngredientRepository.getInstance();
        int n_id = -1;
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("INSERT INTO {0} (id, title, description, category_id, mainPhoto_id, time, difficulty) VALUES (NULL, ?, ?, ?, ?, ?, ?)", tableName));
            statement.setString(1, recipe.getTitle());
            statement.setString(2, recipe.getDescription());
            statement.setInt(3, recipe.getCategory().getID());
            statement.setInt(4, recipe.getPhoto().getID());
            statement.setInt(5, recipe.getTime());
            statement.setInt(6, recipe.getDifficulty());
            statement.executeUpdate();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement2.executeQuery(
                    MessageFormat.format("SELECT * FROM {0} WHERE title=\"{1}\", description=\"{2}\", category_id={3}, mainPhoto_id={4}, time={5}, difficulty={6}",
                            tableName, recipe.getTitle(), recipe.getDescription(),
                            recipe.getCategory().getID(), recipe.getPhoto().getID(),
                            recipe.getTime(), recipe.getDifficulty()));
            while(rs.next()) {
                n_id = rs.getInt("id");
            }
            for (var i: recipe.getSteps()) {
                i.setRecipeID(n_id);
                if (stepRepository.selectByID(i.getID()) == null) {
                    stepRepository.insert(i);
                }
            }
            for (var i: recipe.getIngredientUses()) {
                if (ingredientRepository.selectByID(i.getIngredient().getID()) == null) {
                    ingredientRepository.insert(i.getIngredient());
                }
                if (ingredientRecipeLinkRepository.selectByID(i.getID()) == null) {
                    ingredientRecipeLinkRepository.insert(i);
                }
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        recipe.setID(n_id);
        return n_id;
    }

    public boolean remove(IEntity _entity) {
        if (_entity.getClass() != targetEntityClass) {
            throw new ClassCastException(MessageFormat.format("Expected {0}, got {1}", targetEntityClass.getName(), _entity.getClass().getName()));
        }
        Recipe recipe = (Recipe)_entity;
        Connection conn = getConnection();
        IngredientRecipeLinkRepository ingredientRecipeLinkRepository = IngredientRecipeLinkRepository.getInstance();
        StepRepository stepRepository = StepRepository.getInstance();
        try {
            PreparedStatement statement = conn.prepareStatement(MessageFormat.format("DELETE FROM {0} WHERE id=?", tableName));
            statement.setInt(1, recipe.getID());
            statement.executeUpdate();
            for (var i: recipe.getIngredientUses()) {
                ingredientRecipeLinkRepository.remove(i);
            }
            for (var i: recipe.getSteps()) {
                stepRepository.remove(i);
            }
        }
        catch(SQLException e) { System.err.println(e.getMessage()); }
        return true;
    }

    private static RecipeRepository instance;
    private RecipeRepository() { }
    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }
}
