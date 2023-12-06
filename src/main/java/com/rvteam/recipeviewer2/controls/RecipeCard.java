package com.rvteam.recipeviewer2.controls;

import com.rvteam.recipeviewer2.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;


import java.io.IOException;

public class RecipeCard extends AnchorPane {

    @FXML
    private ImageView mainImageView;

    @FXML
    private Label label_title;

    public RecipeCard(String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("a-recipe-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            label_title.setText(title);

            //Image image = new Image("/com/rvteam/recipeviewer/test.jpg");
            //mainImageView.setImage(image);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}