package com.rvteam.recipeviewer2.data;

public class CartItem implements IEntity {
    private Integer id;
    private Integer ingredientId;
    public CartItem(int _id, int _ingredientId) {
        this.id = _id;
        this.ingredientId = _ingredientId;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }
    public Integer getIngredientID() {
        return ingredientId;
    }
    public void setIngredientID(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }
}
