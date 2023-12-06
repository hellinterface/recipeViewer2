package com.rvteam.recipeviewer2;

import com.rvteam.recipeviewer2.data.*;
import com.rvteam.recipeviewer2.navigation.PageManager;
import com.rvteam.recipeviewer2.navigation.PageList;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Создание таблиц БД
        CartItemRepository.getInstance().createTable();
        CategoryRepository.getInstance().createTable();
        IngredientRepository.getInstance().createTable();
        IngredientRecipeLinkRepository.getInstance().createTable();
        PhotoRepository.getInstance().createTable();
        StepPhotoLinkRepository.getInstance().createTable();
        StepRepository.getInstance().createTable();
        RecipeRepository.getInstance().createTable();
        // Интерфейс
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        PageManager pageController = PageManager.init(stage);
        PageList pageList = new PageList();
        pageController.switchTo(pageList);
    }

    public static void main(String[] args) {
        launch();
    }
}