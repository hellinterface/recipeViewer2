package com.rvteam.recipeviewer2.controls;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.data.Ingredient;
import com.rvteam.recipeviewer2.data.IngredientRepository;
import com.rvteam.recipeviewer2.navigation.SelectIngredient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class IngredientSelectCard extends HBox {

    @FXML
    private TextField textField_name;
    @FXML
    private Button button_select;

    private Ingredient ingredientObject;

    public IngredientSelectCard(Ingredient _ingredient) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("a-ingredient-select-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            textField_name.setText(_ingredient.getName());
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void onRemoveButtonClick() {
        IngredientRepository.getInstance().remove(this.ingredientObject);
    }
    @FXML
    protected void onSelectButtonClick() {
        // save and select
        // this.ingredientObject.set
        SelectIngredient.getInstance().closeSelectionScreen(this.ingredientObject);
    }
}
