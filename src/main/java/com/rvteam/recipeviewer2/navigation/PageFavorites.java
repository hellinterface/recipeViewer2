package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.controls.CartItemCard;
import com.rvteam.recipeviewer2.controls.RecipeCard;
import com.rvteam.recipeviewer2.data.CartItemRepository;
import com.rvteam.recipeviewer2.data.FavoriteRecipeRepository;
import com.rvteam.recipeviewer2.data.Recipe;
import com.rvteam.recipeviewer2.data.RecipeRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PageFavorites extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-favorites.fxml";
    private Scene scene;

    public Scene getPageScene() {
        return scene;
    }

    private void loadScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourceName));
        fxmlLoader.setController(this);
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException exception) {
            System.err.println("Couldn't load FXML: " + resourceName);
            System.err.println(exception);
        }
    }

    public PageFavorites() {
        loadScene();
        FavoriteRecipeRepository favoriteRecipeRepository = FavoriteRecipeRepository.getInstance();
        RecipeRepository recipeRepository = RecipeRepository.getInstance();
        for (var i: favoriteRecipeRepository.selectAll()) {
            Recipe recipe = recipeRepository.selectByID(i.getRecipeID());
            RecipeCard card = new RecipeCard(recipe);
            VBox_mainList.getChildren().add(card);
        }
    }

    @FXML
    VBox VBox_mainList;

    @FXML
    protected void onBackButtonClick() {
        PageManager.getInstance().switchTo(new PageList());
    }
}