package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.data.Ingredient;
import com.rvteam.recipeviewer2.data.Photo;
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

public class PageSelectPhoto extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-select-photo.fxml";
    private Scene scene;

    public Scene getPageScene() {
        return scene;
    }

    private void loadScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourceName));
        fxmlLoader.setController(this);
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException exception) {
            System.err.println("Couldn't load FXML: " + resourceName);
            System.err.println(exception);
        }
    }

    public PageSelectPhoto(List<Photo> _appendList) {
        loadScene();
        PhotoRepository photoRepository = PhotoRepository.getInstance();
        List<Photo> photoList = Stream.concat(photoRepository.selectAll().stream(), _appendList.stream()).toList();
        for (var i: photoList) {
            ImageView imageView = new ImageView();
            imageView.setImage(new Image(new ByteArrayInputStream(i.getBytes())));
            imageView.setFitHeight(140);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    SelectPhoto.getInstance().closeSelectionScreen(i);
                }
            });
            flowPane_imageList.getChildren().add(imageView);
        }
    }

    @FXML
    FlowPane flowPane_imageList;

    @FXML
    protected void onBackButtonClick() {
        SelectPhoto.getInstance().closeSelectionScreen();
    }
}