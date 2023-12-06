package com.rvteam.recipeviewer2.controls;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.data.Ingredient;
import com.rvteam.recipeviewer2.data.IngredientRecipeLink;
import com.rvteam.recipeviewer2.data.IngredientUse;
import com.rvteam.recipeviewer2.navigation.SelectIngredient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class IngredientUseCard extends HBox {

    @FXML
    private TextField textField_name;
    @FXML
    private TextField textField_weight;
    @FXML
    private Button button_remove;

    private IngredientUse ingredientUseObject;

    private Pane parentElement;

    private void refreshIngredient() {
        textField_name.setText(ingredientUseObject.getIngredient().getName());
    }

    public IngredientUseCard(IngredientUse _ingredientUse, Pane _parentElement) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("a-ingredient-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.parentElement = _parentElement;
        this.ingredientUseObject = _ingredientUse;
        try {
            fxmlLoader.load();
            textField_weight.setText(_ingredientUse.getWeight().toString());
            refreshIngredient();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public IngredientUse getIngredientUseObject() {
        return this.ingredientUseObject;
    }

    @FXML
    protected void onRemoveButtonClick() {
        this.parentElement.getChildren().remove(this);
    }

    @FXML
    protected void onChangeIngredientButtonClick() {
        Function<Ingredient, Integer> onSelectionClose = (Ingredient ing) -> {
            this.ingredientUseObject.setIngredient(ing);
            refreshIngredient();
            return 0;
        };
        List<Ingredient> list = new ArrayList<Ingredient>();
        list.add(this.ingredientUseObject.getIngredient());
        SelectIngredient.getInstance().openSelectionScreen(onSelectionClose, list);
    }
}
