package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.controls.RecipeCard;
import com.rvteam.recipeviewer2.foodruParser.FoodRuParser;
import com.rvteam.recipeviewer2.foodruParser.IParser;
import com.rvteam.recipeviewer2.foodruParser.ParserResult;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PageList extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-list.fxml";
    private Scene scene;
    public Scene getPageScene() {
        return scene;
    }
    public PageList() {
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
    private VBox mainList;

    protected void onHelloButtonClick() {
        try {
            IParser foodRuParser = new FoodRuParser();
            ParserResult parserResult = foodRuParser.parseURL("https://food.ru/recipes/192011-plov-so-svininoy-v-multivarke");
            parserResult.DEBUG_print();
            RecipeCard newCard = new RecipeCard("test123");
            mainList.getChildren().add(newCard);
        }
        catch (IOException exception) {

        }
    }

    @FXML
    protected void onImportButtonClick() {
        PageManager.getInstance().switchTo(new PageParserURL());
    }
}
