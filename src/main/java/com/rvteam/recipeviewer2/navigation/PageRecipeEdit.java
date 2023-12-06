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
import java.util.function.Function;

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
        }
        catch (IOException exception) {
            System.err.println("Couldn't load FXML: " + resourceName);
            System.err.println(exception);
        }
    }

    public PageRecipeEdit() {
        loadScene();
        this.recipeObject = new Recipe(-1, "", "", null, null, 0, 1, new ArrayList<IngredientUse>(), new ArrayList<Step>());
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
        boolean categoryExists = false;
        for (var i: categoryList) {
            if (i.getID() == recipeCategory.getID()) {
                categoryExists = true;
                break;
            }
        }
        if (!categoryExists) {
            categoryList.add(recipeCategory); // добавить в список категорию из объекта рецепта, если его не нашлось в БД
        }
        choiceBox_category.setItems(categoryList); // поставить список для элемента
        choiceBox_category.getSelectionModel().select(recipeCategory); // поставить категорию из рецепта как выбранную по умолчанию
        // Сложность ---------------------------------------------------------------------------------
        ObservableList<Integer> difficultyList = FXCollections.observableArrayList(1,2,3,4,5);
        choiceBox_difficulty.setItems(difficultyList); // поставить список для элемента
        choiceBox_difficulty.getSelectionModel().select(recipeObject.getDifficulty()); // поставить указанную сложность
        // Фотография ---------------------------------------------------------------------------------
        imageView_mainPhoto.setFitHeight(100);
        imageView_mainPhoto.setPreserveRatio(true);
        if (recipeObject.getPhoto() != null) {
            Image image = new Image(new ByteArrayInputStream(recipeObject.getPhoto().getBytes()));
            System.out.println(recipeObject.getPhoto().getBytes().length);
            imageView_mainPhoto.setImage(image);
        }
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
            IngredientUseCard ingredientUseCard = new IngredientUseCard(i, VBox_ingredientsContainer);
            VBox_ingredientsContainer.getChildren().add(ingredientUseCard);
        }
        label_calories.setText("К " + String.valueOf(calories));
        label_bzu_b.setText("Б " + String.valueOf(b));
        label_bzu_z.setText("Ж " + String.valueOf(z));
        label_bzu_u.setText("У " + String.valueOf(u));
        // Шаги ---------------------------------------------------------------------------------
        List<Step> steps = recipeObject.getSteps();
        for (var i: steps) {
            StepCard stepCard = new StepCard(i, VBox_stepsContainer);
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
    protected void saveAndExit() { // Сохранить и выйти
        if (recipeObject.getPhoto() == null) {
            recipeObject.setPhoto(new Photo(-1, new byte[]{}));
        }
        recipeObject.setTitle(textField_title.getText()); // Название
        recipeObject.setDescription(textArea_description.getText()); // Описание
        Category selectedCategory = choiceBox_category.getSelectionModel().getSelectedItem();
        recipeObject.setCategory(selectedCategory); // Категория
        int time = 0;
        time += Integer.parseInt(textField_time_hours.getText()) * 60;
        time += Integer.parseInt(textField_time_minutes.getText());
        recipeObject.setTime(time); // Время
        recipeObject.setSteps(new ArrayList<Step>()); // Очистка списка с шагами
        for (var i: VBox_stepsContainer.getChildren()) {
            if (i.getClass() == StepCard.class) {
                StepCard card = (StepCard)i;
                recipeObject.getSteps().add(card.getStepObject()); // Добавление шага
            }
        }
        recipeObject.setIngredientUses(new ArrayList<IngredientUse>()); // Очистка списка с ингредиентами
        for (var i: VBox_ingredientsContainer.getChildren()) {
            if (i.getClass() == IngredientUseCard.class) {
                IngredientUseCard card = (IngredientUseCard)i;
                recipeObject.getIngredientUses().add(card.getIngredientUseObject()); // Добавление ингредиента
            }
        }
        recipeObject.setDifficulty(choiceBox_difficulty.getSelectionModel().getSelectedItem()); // Сложность
        RecipeRepository.getInstance().push(recipeObject);
        PageManager.getInstance().switchTo(new PageList());
    }
    @FXML
    protected void exitWithoutSaving() { // Выход без сохранения
        PageManager.getInstance().switchTo(new PageList());
    }
    @FXML
    protected void removeAndExit() { // Удаление рецепта
        RecipeRepository.getInstance().remove(recipeObject);
        PageManager.getInstance().switchTo(new PageList());
    }

    @FXML
    protected void onMainPhotoClick() { // Нажатие на фотографию сверху
        Function<Photo, Integer> onSelectionClose = (Photo photo) -> {
            recipeObject.setPhoto(photo);
            return 0;
        };
        List<Photo> photoList = new ArrayList<Photo>();
        photoList.add(recipeObject.getPhoto());
        SelectPhoto.getInstance().openSelectionScreen(onSelectionClose, photoList);
    }
    @FXML
    protected void onAddStepButtonClick() { // Добавление пустого шага
        Step step = new Step(-1, recipeObject.getID(), "text", new ArrayList<Photo>());
        StepCard card = new StepCard(step, VBox_stepsContainer);
        VBox_stepsContainer.getChildren().add(card);
    }
    @FXML
    protected void onAddIngredientButtonClick() { // Добавление пустого ингредиента
        float zero = 0;
        Ingredient ingredient = new Ingredient(-1, "", zero, zero, zero, zero);
        IngredientUse ingredientUse = new IngredientUse(-1, ingredient, zero);
        IngredientUseCard card = new IngredientUseCard(ingredientUse, VBox_ingredientsContainer);
        VBox_ingredientsContainer.getChildren().add(card);
    }
}
