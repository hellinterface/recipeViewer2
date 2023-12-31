package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.data.Ingredient;
import com.rvteam.recipeviewer2.data.StepPhotoLinkRepository;
import javafx.scene.Scene;

import java.util.List;
import java.util.function.Function;

public class SelectIngredient {
    private Scene previousScene;
    private Function<Ingredient, Integer> onClose;
    public void openSelectionScreen(Function<Ingredient, Integer> _onClose, List<Ingredient> _appendList) {
        PageManager pageManager = PageManager.getInstance();
        previousScene = pageManager.getCurrentScene();
        this.onClose = _onClose;
        pageManager.switchTo(new PageSelectIngredient(_appendList));
    }
    public void closeSelectionScreen() {
        PageManager.getInstance().switchTo(previousScene);
    }
    public void closeSelectionScreen(Ingredient _ingredient) {
        onClose.apply(_ingredient);
        PageManager.getInstance().switchTo(previousScene);
    };

    private static SelectIngredient instance;
    private SelectIngredient() { }
    public static SelectIngredient getInstance() {
        if (instance == null) {
            instance = new SelectIngredient();
        }
        return instance;
    }
}
