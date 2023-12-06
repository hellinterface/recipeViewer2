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
import javafx.scene.layout.Pane;

import java.io.IOException;

public class IngredientSelectCard extends HBox {

    @FXML
    private TextField textField_name;
    @FXML
    private TextField textField_calories;
    @FXML
    private TextField textField_b;
    @FXML
    private TextField textField_z;
    @FXML
    private TextField textField_u;

    private Ingredient ingredientObject;
    private Pane parentElement;

    public IngredientSelectCard(Ingredient _ingredient, Pane _parentElement) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("a-ingredient-select-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.ingredientObject = _ingredient;
        this.parentElement = _parentElement;
        try {
            fxmlLoader.load();
            textField_name.setText(_ingredient.getName());
            textField_calories.setText(_ingredient.getCalories().toString());
            textField_b.setText(_ingredient.getBzuB().toString());
            textField_z.setText(_ingredient.getBzuZ().toString());
            textField_u.setText(_ingredient.getBzuU().toString());
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void onRemoveButtonClick() {
        try {
            IngredientRepository.getInstance().remove(this.ingredientObject);
            this.parentElement.getChildren().remove(this);
        }
        catch (Exception exception) {
            System.err.println(exception);
        }
    }
    @FXML
    protected void onSelectButtonClick() {
        this.ingredientObject.setName(textField_name.getText());
        this.ingredientObject.setCalories(Float.parseFloat(textField_calories.getText()));
        this.ingredientObject.setBzuB(Float.parseFloat(textField_b.getText()));
        this.ingredientObject.setBzuZ(Float.parseFloat(textField_z.getText()));
        this.ingredientObject.setBzuU(Float.parseFloat(textField_u.getText()));
        SelectIngredient.getInstance().closeSelectionScreen(this.ingredientObject);
    }
}
