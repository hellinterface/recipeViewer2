package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.data.Photo;
import javafx.scene.Scene;

import java.util.List;
import java.util.function.Function;

public class SelectPhoto {
    private Scene previousScene;
    private Function<Photo, Integer> onClose;
    public void openSelectionScreen(Function<Photo, Integer> _onClose, List<Photo> _appendList) {
        PageManager pageManager = PageManager.getInstance();
        previousScene = pageManager.getCurrentScene();
        this.onClose = _onClose;
        pageManager.switchTo(new PageSelectPhoto(_appendList));
    };
    public void closeSelectionScreen() {
        PageManager.getInstance().switchTo(previousScene);
    }
    public void closeSelectionScreen(Photo _photo) {
        onClose.apply(_photo);
        PageManager.getInstance().switchTo(previousScene);
    };

    private static SelectPhoto instance;
    private SelectPhoto() { }
    public static SelectPhoto getInstance() {
        if (instance == null) {
            instance = new SelectPhoto();
        }
        return instance;
    }
}
