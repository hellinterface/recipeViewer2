package com.rvteam.recipeviewer2.data;

public class IngredientRecipeLink implements IEntity {
    private Integer id;
    private Integer recipeId;
    private Integer ingredientId;
    private Float weight;

    public IngredientRecipeLink(int _id, int _recipeId, int _ingredientId, float _weight) {
        id = _id;
        recipeId = _recipeId;
        ingredientId = _ingredientId;
        weight = _weight;
    }

    public Integer getID() {
        return id;
    }
    public void setID(int n) {
        this.id = n;
    }

    public Integer getIngredientID() {
        return ingredientId;
    }

    public Integer getRecipeID() {
        return recipeId;
    }

    public Float getWeight() {
        return weight;
    }
}
