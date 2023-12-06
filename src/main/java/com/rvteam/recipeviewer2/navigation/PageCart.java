package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.controls.CartItemCard;
import com.rvteam.recipeviewer2.controls.IngredientSelectCard;
import com.rvteam.recipeviewer2.data.CartItemRepository;
import com.rvteam.recipeviewer2.data.Ingredient;
import com.rvteam.recipeviewer2.data.IngredientRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class PageCart extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-cart.fxml";
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

    public PageCart() {
        loadScene();
        CartItemRepository cartItemRepository = CartItemRepository.getInstance();
        for (var i: cartItemRepository.selectAll()) {
            CartItemCard card = new CartItemCard(i, VBox_mainList);
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