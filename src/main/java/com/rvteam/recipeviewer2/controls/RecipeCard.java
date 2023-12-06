package com.rvteam.recipeviewer2.controls;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.data.Recipe;
import com.rvteam.recipeviewer2.navigation.PageManager;
import com.rvteam.recipeviewer2.navigation.PageRecipeEdit;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;


import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RecipeCard extends AnchorPane {

    @FXML
    private ImageView mainImageView;

    @FXML
    private Label label_title;
    @FXML
    private Label label_category;
    @FXML
    private Label label_difficulty;
    @FXML
    private Label label_ingredients;
    @FXML
    private Label label_calories;

    public RecipeCard(Recipe recipe) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("a-recipe-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            if (recipe.getPhoto() != null) {
                Image image = new Image(new ByteArrayInputStream(recipe.getPhoto().getBytes()));
                mainImageView.setImage(image);
            }
            label_title.setText(recipe.getTitle());
            if (recipe.getCategory() != null) {
                label_category.setText("Категория: " + recipe.getCategory().getTitle());
            }
            else {
                label_category.setText("Категория: ???");
            }
            label_difficulty.setText("Сложность: " + recipe.getDifficulty());
            label_ingredients.setText(recipe.getIngredientUses().size() + " ингредиентов");
            float calories = 0;
            for (var i: recipe.getIngredientUses()) {
                if (i.getIngredient() != null) {
                    calories += i.getIngredient().getCalories() * i.getWeight();
                }
            }
            label_calories.setText(calories + " калорий");
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    PageManager.getInstance().switchTo(new PageRecipeEdit(recipe));
                }
            });
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}