package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.data.*;
import com.rvteam.recipeviewer2.foodruParser.FoodRuParser;
import com.rvteam.recipeviewer2.foodruParser.IParser;
import com.rvteam.recipeviewer2.foodruParser.ParserResult;
import com.rvteam.recipeviewer2.foodruParser.RecipeConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageParserURL extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-parser-url.fxml";
    private Scene scene;
    public Scene getPageScene() {
        return scene;
    }
    public PageParserURL() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourceName));
        fxmlLoader.setController(this);
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException exception) {
            System.err.println("Couldn't load FXML: " + resourceName);
        }
    }

    /*
     *        Controller
     */

    @FXML
    Button startImportButton;
    @FXML
    TextField textField_url;
    @FXML
    Label label_status;
    @FXML
    protected void onStartImportButtonClick() {
        label_status.setText("Подождите...");
        startImportButton.setDisable(true);
        IParser parser = new FoodRuParser();
        String url = textField_url.getText();
        try {
            ParserResult result = parser.parseURL(url);
            Recipe recipe = RecipeConverter.convertFromParserResult(result);
            PageManager.getInstance().switchTo(new PageRecipeEdit(recipe));
        }
        catch (Exception exception) {
            label_status.setText("Не удалось импортировать рецепт");
            startImportButton.setDisable(false);
        }
    }
    @FXML
    protected void onCreateNewButtonClick() {
        PageManager.getInstance().switchTo(new PageRecipeEdit());
    }
    @FXML
    protected void onBackButtonClick() {
        PageManager.getInstance().switchTo(new PageList());
    }
}
