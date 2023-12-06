package com.rvteam.recipeviewer2.controls;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.data.CartItem;
import com.rvteam.recipeviewer2.data.CartItemRepository;
import com.rvteam.recipeviewer2.data.Ingredient;
import com.rvteam.recipeviewer2.data.IngredientRepository;
import com.rvteam.recipeviewer2.navigation.SelectIngredient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CartItemCard extends HBox {

    @FXML
    private TextField textField_name;

    private CartItem cartItemObject;
    private Pane parentElement;

    public CartItemCard(CartItem _cartItem, Pane _parentElement) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("a-cart-item-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.cartItemObject = _cartItem;
        this.parentElement = _parentElement;
        try {
            fxmlLoader.load();
            Ingredient ingredient = IngredientRepository.getInstance().selectByID(_cartItem.getIngredientID());
            textField_name.setText(ingredient.getName());
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void onRemoveButtonClick() {
        try {
            CartItemRepository.getInstance().remove(this.cartItemObject);
            this.parentElement.getChildren().remove(this);
        }
        catch (Exception exception) {
            System.err.println(exception);
        }
    }
}
