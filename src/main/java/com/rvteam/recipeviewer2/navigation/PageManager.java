package com.rvteam.recipeviewer2.navigation;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class PageManager {
    private Stage stage = new Stage();

    //Class<? extends IPage> _pageClass
    public void switchTo(Scene _scene) {
        try {
            stage.setScene(_scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void switchTo(IPage _page) {
        try {
            stage.setScene(_page.getPageScene());
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Scene getCurrentScene() {
        return stage.getScene();
    }

    private static PageManager instance;
    private PageManager() { }
    public static PageManager init(Stage _stage) {
        if (instance == null) {
            instance = new PageManager();
            instance.stage = _stage;
        }
        return instance;
    }
    public static PageManager getInstance() {
        if (instance == null) {
            instance = new PageManager();
        }
        return instance;
    }
}