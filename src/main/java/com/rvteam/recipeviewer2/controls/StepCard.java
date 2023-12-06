package com.rvteam.recipeviewer2.controls;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.data.Photo;
import com.rvteam.recipeviewer2.data.Step;
import com.rvteam.recipeviewer2.navigation.PageManager;
import com.rvteam.recipeviewer2.navigation.SelectPhoto;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class StepCard extends VBox {

    @FXML
    private HBox HBox_imageReel;
    @FXML
    private Label label_title;
    @FXML
    private TextArea textArea_text;

    private Step stepObject;

    private void refreshImages() {
        HBox_imageReel.getChildren().clear();
        for (var i : stepObject.getPhotos()) {
            Image image = new Image(new ByteArrayInputStream(i.getBytes()));
            ImageView imageView = new ImageView();
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            imageView.setImage(image);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Function<Photo, Integer> onSelectionClose = (Photo photo) -> {
                        int index = stepObject.getPhotos().indexOf(i);
                        stepObject.getPhotos().remove(i);
                        stepObject.getPhotos().add(index, photo);
                        return 0;
                    };
                    SelectPhoto.getInstance().openSelectionScreen(onSelectionClose);
                }
            });
            HBox_imageReel.getChildren().add(imageView);
        }
    }

    public StepCard(Step step) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("a-step-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            stepObject = step;
            textArea_text.setText(step.getText());
            refreshImages();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    public Step getStepObject() {
        return this.stepObject;
    }

    @FXML
    protected void onTextInput() {
        System.out.println("A");
        stepObject.setText(textArea_text.getText());
    }

    @FXML
    protected void onAddPhotoButtonClick() {
        Function<Photo, Integer> onSelectionClose = (Photo photo) -> {
            stepObject.getPhotos().add(photo);
            return 0;
        };
        SelectPhoto.getInstance().openSelectionScreen(onSelectionClose);
    }
}
