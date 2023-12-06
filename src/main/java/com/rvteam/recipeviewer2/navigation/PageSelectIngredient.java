package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.controls.IngredientSelectCard;
import com.rvteam.recipeviewer2.data.Ingredient;
import com.rvteam.recipeviewer2.data.IngredientRepository;
import com.rvteam.recipeviewer2.data.PhotoRepository;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class PageSelectIngredient extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-select-ingredient.fxml";
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

    public PageSelectIngredient(List<Ingredient> _appendList) {
        loadScene();
        IngredientRepository ingredientRepository = IngredientRepository.getInstance();
        List<Ingredient> ingredientList = Stream.concat(ingredientRepository.selectAll().stream(), _appendList.stream()).toList();
        for (var i: ingredientList) {
            IngredientSelectCard card = new IngredientSelectCard(i, VBox_mainList);
            VBox_mainList.getChildren().add(card);
        }
    }

    @FXML
    VBox VBox_mainList;

    @FXML
    protected void onBackButtonClick() {
        SelectIngredient.getInstance().closeSelectionScreen();
    }
}