package com.rvteam.recipeviewer2.data;

import java.util.List;

public class Step implements IEntity {
    private Integer id;
    private Integer recipe_id;
    private String text;
    private List<Photo> photos;
    public Step(Integer id, Integer recipe_id, String text, List<Photo> photos) {
        this.id = id;
        this.recipe_id = recipe_id;
        this.text = text;
        this.photos = photos;
    }
    public Integer getID() { return this.id; }
    public void setID(int _id) {
        id = _id;
    }
    public Integer getRecipeID() { return this.recipe_id; }
    public void setRecipeID(int _id) {
        recipe_id = _id;
    }
    public String getText() { return this.text; }
    public void setText(String _text) {
        this.text = _text;
    }
    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> _photos) {
        this.photos = _photos;
    }
}
