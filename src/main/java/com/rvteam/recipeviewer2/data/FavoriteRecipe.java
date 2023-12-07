package com.rvteam.recipeviewer2.data;

public class FavoriteRecipe implements IEntity {
    private Integer id;
    private Integer recipeId;
    public FavoriteRecipe(int _id, int _recipeId) {
        this.id = _id;
        this.recipeId = _recipeId;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }
    public Integer getRecipeID() {
        return recipeId;
    }
    public void setRecipeID(Integer recipeId) {
        this.recipeId = recipeId;
    }
}
