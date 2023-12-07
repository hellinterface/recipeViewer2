package com.rvteam.recipeviewer2.navigation;

import com.rvteam.recipeviewer2.MainApplication;
import com.rvteam.recipeviewer2.controls.RecipeCard;
import com.rvteam.recipeviewer2.data.CategoryRepository;
import com.rvteam.recipeviewer2.data.Recipe;
import com.rvteam.recipeviewer2.data.RecipeRepository;
import com.rvteam.recipeviewer2.foodruParser.FoodRuParser;
import com.rvteam.recipeviewer2.foodruParser.IParser;
import com.rvteam.recipeviewer2.foodruParser.ParserResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Filter;

class FilterValue {
    public String visibleTitle;
    public Integer value;
    public FilterValue(String _visibleTitle, Integer _value) {
        this.visibleTitle = _visibleTitle;
        this.value = _value;
    }
    @Override
    public String toString() {
        return visibleTitle;
    }
}
class FilterKey {
    public String visibleTitle;
    public Function<Integer, List<Recipe>> selectFunction;
    public Function<Integer, ObservableList<FilterValue>> getFilterValueListFunction;
    public FilterKey(String _visibleTitle, Function<Integer, List<Recipe>> _selectFunction, Function<Integer, ObservableList<FilterValue>> _getFilterValueListFunction) {
        this.visibleTitle = _visibleTitle;
        this.selectFunction = _selectFunction;
        this.getFilterValueListFunction = _getFilterValueListFunction;
    }
    public FilterKey(String _visibleTitle, Function<Integer, List<Recipe>> _selectFunction, ObservableList<FilterValue> _filterValueList) {
        Function<Integer, ObservableList<FilterValue>> filterValueFunction = (Integer n) -> { return _filterValueList; };
        this.visibleTitle = _visibleTitle;
        this.selectFunction = _selectFunction;
        this.getFilterValueListFunction = filterValueFunction;
    }
    public FilterKey(String _visibleTitle, Function<Integer, List<Recipe>> _selectFunction) {
        this(_visibleTitle, _selectFunction, FXCollections.observableArrayList());
    }
    public void onSelect(ChoiceBox<FilterValue> choiceBox) {
        choiceBox.setItems(getFilterValueListFunction.apply(0));
        choiceBox.getSelectionModel().select(0);
    }
    @Override
    public String toString() {
        return visibleTitle;
    }
}
class SortMethod {
    public String visibleTitle;
    public Comparator<Recipe> comparator;
    public SortMethod(String _visibleTitle, Comparator<Recipe> _comparator) {
        this.visibleTitle = _visibleTitle;
        this.comparator = _comparator;
    }
    @Override
    public String toString() {
        return visibleTitle;
    }
}

public class PageList extends VBox implements IPage {
    /*
     *        Init
     */

    private final String resourceName = "page-list.fxml";
    private Scene scene;
    private boolean ready;
    public Scene getPageScene() {
        return scene;
    }
    public PageList() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourceName));
        fxmlLoader.setController(this);
        try {
            scene = new Scene(fxmlLoader.load());
            RecipeRepository recipeRepository = RecipeRepository.getInstance();
            Function<Integer, ObservableList<FilterValue>> categoryFunction = (Integer n) -> {
                List<FilterValue> resultList = new ArrayList<FilterValue>();
                for (var i: CategoryRepository.getInstance().selectAll()) {
                    resultList.add( new FilterValue(i.getTitle(), i.getID()) );
                }
                return FXCollections.observableArrayList(resultList);
            };
            ObservableList<FilterKey> filterKeyList = FXCollections.observableArrayList(
                    new FilterKey("Без фильтрации", recipeRepository::selectAll),
                    new FilterKey("Категория", recipeRepository::selectByCategoryID, categoryFunction),
                    new FilterKey("Сложность", recipeRepository::selectByDifficulty, FXCollections.observableArrayList(
                            new FilterValue("1", 1),
                            new FilterValue("2", 2),
                            new FilterValue("3", 3),
                            new FilterValue("4", 4),
                            new FilterValue("5", 5)
                    ))
            );
            choiceBox_filterKey.setItems(filterKeyList);
            choiceBox_filterKey.getSelectionModel().selectFirst();
            ObservableList<SortMethod> sortMethodList = FXCollections.observableArrayList(
                    new SortMethod("По дате добавления ˅", (Recipe a, Recipe b) -> b.getID() - a.getID()),
                    new SortMethod("По дате добавления ˄", (Recipe a, Recipe b) -> a.getID() - b.getID()),
                    new SortMethod("По алфавиту ˅", (Recipe a, Recipe b) -> a.getTitle().compareToIgnoreCase(b.getTitle())),
                    new SortMethod("По алфавиту ˄", (Recipe a, Recipe b) -> -1*(a.getTitle().compareToIgnoreCase(b.getTitle()))),
                    new SortMethod("По сложности ˅", (Recipe a, Recipe b) -> b.getDifficulty() - a.getDifficulty()),
                    new SortMethod("По сложности ˄", (Recipe a, Recipe b) -> a.getDifficulty() - b.getDifficulty()),
                    new SortMethod("По категории ˅", (Recipe a, Recipe b) -> b.getCategory().getID() - a.getCategory().getID()),
                    new SortMethod("По категории ˄", (Recipe a, Recipe b) -> a.getCategory().getID() - b.getCategory().getID()),
                    new SortMethod("По количеству ингредиентов ˅", (Recipe a, Recipe b) -> b.getIngredientUses().size() - a.getIngredientUses().size()),
                    new SortMethod("По количеству ингредиентов ˄", (Recipe a, Recipe b) -> a.getIngredientUses().size() - b.getIngredientUses().size())
            );
            choiceBox_sortBy.setItems(sortMethodList);
            choiceBox_sortBy.getSelectionModel().select(0);
            loadRecipeList();
            ready = true;
        }
        catch (IOException exception) {
            System.err.println("Couldn't load FXML: " + resourceName);
        }
    }

    private void loadRecipeList() {
        VBox_mainList.getChildren().clear();
        // Получение целевого значения фильтрации
        FilterValue filterValue = choiceBox_filterValue.getSelectionModel().getSelectedItem();
        if (filterValue == null) filterValue = new FilterValue("", 0);
        // Запрос в БД с указанной фильтрацией
        List<Recipe> recipeList = choiceBox_filterKey.getSelectionModel().getSelectedItem().selectFunction.apply(filterValue.value);
        // Сортировка списка
        recipeList.sort(choiceBox_sortBy.getSelectionModel().getSelectedItem().comparator);
        // Создание карточек
        for (var i: recipeList) {
            VBox_mainList.getChildren().add(new RecipeCard(i));
        }
    }

    /*
    *        Controller
    */

    @FXML
    private VBox VBox_mainList;
    @FXML
    private ChoiceBox<FilterKey> choiceBox_filterKey;
    @FXML
    private ChoiceBox<FilterValue> choiceBox_filterValue;
    @FXML
    private ChoiceBox<SortMethod> choiceBox_sortBy;

    @FXML
    protected void onImportButtonClick() {
        PageManager.getInstance().switchTo(new PageParserURL());
    }
    @FXML
    protected void onFilterKeyChoiceBoxSelectedChange() {
        if (this.ready == false) return;
        choiceBox_filterKey.getSelectionModel().getSelectedItem().onSelect(choiceBox_filterValue);
        loadRecipeList();
    }
    @FXML
    protected void onFilterValueChoiceBoxSelectedChange() {
        if (this.ready == false) return;
        loadRecipeList();
    }
    @FXML
    protected void onSortByChoiceBoxSelectedChange() {
        if (this.ready == false) return;
        loadRecipeList();
    }
    @FXML
    protected void onCartButtonClick() {
        PageManager.getInstance().switchTo(new PageCart());
    }

    @FXML
    protected void onFavoritesButtonClick() {
        PageManager.getInstance().switchTo(new PageFavorites());
    }
}
