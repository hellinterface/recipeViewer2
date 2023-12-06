package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.controls.IngredientUseCard;
import com.rvteam.recipeviewer2.controls.StepCard;
import com.rvteam.recipeviewer2.data.*;
import com.rvteam.recipeviewer2.foodruParser.ParserResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageRecipeEdit extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-recipe-edit.fxml";
    private Scene scene;

    private Recipe recipeObject;
    public Scene getPageScene() {
        return scene;
    }
    private void loadScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourceName));
        fxmlLoader.setController(this);
        try {
            scene = new Scene(fxmlLoader.load());
            System.out.println("---------- LOADED A SCENE");
        }
        catch (IOException exception) {
            System.err.println("Couldn't load FXML: " + resourceName);
            System.err.println(exception);
        }
    }

    public PageRecipeEdit() {
        loadScene();
        // Категория ---------------------------------------------------------------------------------
        ObservableList<Category> categoryList = FXCollections.observableList(CategoryRepository.getInstance().selectAll()); // преобразования обычного списка в наблюдаемый
        choiceBox_category.setItems(categoryList); // поставить список для элемента
        choiceBox_category.getSelectionModel().select(0); // поставить категорию из рецепта как выбранную по умолчанию
        // Сложность ---------------------------------------------------------------------------------
        ObservableList<Integer> difficultyList = FXCollections.observableArrayList(1,2,3,4,5);
        choiceBox_difficulty.setItems(difficultyList); // поставить список для элемента
        choiceBox_difficulty.getSelectionModel().select(0); // поставить указанную сложность
    }
    public PageRecipeEdit(Recipe _recipe) {
        loadScene();
        recipeObject = _recipe;
        // Название и описание ---------------------------------------------------------------------------------
        textField_title.setText(recipeObject.getTitle());
        textArea_description.setText(recipeObject.getDescription());
        // Категория ---------------------------------------------------------------------------------
        ObservableList<Category> categoryList = FXCollections.observableList(CategoryRepository.getInstance().selectAll()); // преобразования обычного списка в наблюдаемый
        Category recipeCategory = recipeObject.getCategory();
        if (!categoryList.contains(recipeCategory)) categoryList.add(recipeCategory); // добавить в список категорию из объекта рецепта, если его не нашлось в БД
        choiceBox_category.setItems(categoryList); // поставить список для элемента
        choiceBox_category.getSelectionModel().select(recipeCategory); // поставить категорию из рецепта как выбранную по умолчанию
        // Сложность ---------------------------------------------------------------------------------
        ObservableList<Integer> difficultyList = FXCollections.observableArrayList(1,2,3,4,5);
        choiceBox_difficulty.setItems(difficultyList); // поставить список для элемента
        choiceBox_difficulty.getSelectionModel().select(recipeObject.getDifficulty()); // поставить указанную сложность
        // Фотография ---------------------------------------------------------------------------------
        imageView_mainPhoto.setFitHeight(100);
        imageView_mainPhoto.setPreserveRatio(true);
        Image image = new Image(new ByteArrayInputStream(recipeObject.getPhoto().getBytes()));
        System.out.println(recipeObject.getPhoto().getBytes().length);
        imageView_mainPhoto.setImage(image);
        // Время ---------------------------------------------------------------------------------
        int time = recipeObject.getTime();
        int time_hours = time / 60;
        int time_minutes = time % 60;
        textField_time_hours.setText(String.valueOf(time_hours));
        textField_time_minutes.setText(String.valueOf(time_minutes));
        // Ингредиенты и КБЖУ ---------------------------------------------------------------------------------
        float calories = 0;
        float b = 0;
        float z = 0;
        float u = 0;
        for (var i : recipeObject.getIngredientUses()) {
            Ingredient ing = i.getIngredient();
            float w = i.getWeight();
            calories += w * ing.getCalories();
            b += w * ing.getBzuB();
            z += w * ing.getBzuZ();
            u += w * ing.getBzuU();
            IngredientUseCard ingredientUseCard = new IngredientUseCard(i);
            VBox_ingredientsContainer.getChildren().add(ingredientUseCard);
        }
        label_calories.setText("К " + String.valueOf(calories));
        label_bzu_b.setText("Б " + String.valueOf(b));
        label_bzu_z.setText("Ж " + String.valueOf(z));
        label_bzu_u.setText("У " + String.valueOf(u));
        // Шаги ---------------------------------------------------------------------------------
        List<Step> steps = recipeObject.getSteps();
        for (int i = 0; i < steps.size(); i++) {
            StepCard stepCard = new StepCard("Шаг "+(i+1), steps.get(i));
            VBox_stepsContainer.getChildren().add(stepCard);
        }
    }

    /*
     *        Controller
     */

    @FXML
    TextField textField_title;
    @FXML
    TextArea textArea_description;
    @FXML
    ImageView imageView_mainPhoto;
    @FXML
    ChoiceBox<Category> choiceBox_category;
    @FXML
    ChoiceBox<Integer> choiceBox_difficulty;
    @FXML
    TextField textField_time_hours;
    @FXML
    TextField textField_time_minutes;
    @FXML
    Label label_calories;
    @FXML
    Label label_bzu_b;
    @FXML
    Label label_bzu_z;
    @FXML
    Label label_bzu_u;
    @FXML
    VBox VBox_ingredientsContainer;
    @FXML
    VBox VBox_stepsContainer;

    @FXML
    protected void onAddIngredientButtonClick() {

    }

    @FXML
    protected void saveAndExit() {
        recipeObject.setTitle(textField_title.getText());
        recipeObject.setDescription(textArea_description.getText());
        Category selectedCategory = choiceBox_category.getSelectionModel().getSelectedItem();
        recipeObject.setCategory(selectedCategory); // get object from choicebox
        Image image = imageView_mainPhoto.getImage();
        /*
        recipeObject.setPhoto(); // get src of photo from imageview
        int time = 0;
        time += Integer.parseInt(textField_time_hours.getText()) * 60;
        time += Integer.parseInt(textField_time_minutes.getText());
        recipeObject.setTime(time);
        recipeObject.setIngredientUses();
        recipeObject.setSteps();
         */
        recipeObject.setSteps(new ArrayList<Step>());
        for (var i: VBox_stepsContainer.getChildren()) {
            if (i.getClass() == StepCard.class) {
                StepCard card = (StepCard)i;
                recipeObject.getSteps().add(card.getStepObject());
            }
        }
        recipeObject.setIngredientUses(new ArrayList<IngredientUse>());
        for (var i: VBox_ingredientsContainer.getChildren()) {
            if (i.getClass() == IngredientUseCard.class) {
                IngredientUseCard card = (IngredientUseCard)i;
                recipeObject.getIngredientUses().add(card.getIngredientUseObject());
            }
        }
    }
    @FXML
    protected void exitWithoutSaving() {

    }

    @FXML
    protected void onMainPhotoClick() {
        PageManager.getInstance().switchTo(new PageSelectPhoto());
    }
}
