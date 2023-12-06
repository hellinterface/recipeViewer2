package com.rvteam.recipeviewer2.data;

public class IngredientUse implements IEntity {
    private Integer id;
    private Ingredient ingredient;
    private Float weight;

    public IngredientUse(Integer _id, Ingredient _ingredient, float _weight) {
        id = _id;
        ingredient = _ingredient;
        weight = _weight;
    }

    @Override
    public Integer getID() {
        return id;
    }
    public void setID(int n) {
        this.id = n;
    }
    public Ingredient getIngredient() {
        return ingredient;
    }
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
    public Float getWeight() {
        return weight;
    }
    public void setWeight(Float weight) {
        this.weight = weight;
    }
}