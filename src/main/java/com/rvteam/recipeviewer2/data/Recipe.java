package com.rvteam.recipeviewer2.data;

import java.util.List;

public class Recipe implements IEntity {
    private Integer id;
    private String title;
    private String description;
    private Category category;
    private Photo mainPhoto;
    private Integer time;
    private List<IngredientUse> ingredientUses;
    private Integer difficulty;
    private List<Step> steps;

    public Recipe(Integer _id, String _title, String _description, Category _category, Photo _mainPhoto, Integer _time, Integer _difficulty, List<IngredientUse> _ingredients, List<Step> _steps) {
        id = _id;
        title = _title;
        description = _description;
        category = _category;
        mainPhoto = _mainPhoto;
        time = _time;
        difficulty = _difficulty;
        ingredientUses = _ingredients;
        steps = _steps;
    }

    public Integer getID() {
        return id;
    }

    public void setID(int _id) {
        id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Photo getPhoto() {
        return this.mainPhoto;
    }

    public void setPhoto(Photo mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public List<IngredientUse> getIngredientUses() {
        return ingredientUses;
    }

    public void setIngredientUses(List<IngredientUse> ingredientUses) {
        this.ingredientUses = ingredientUses;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}