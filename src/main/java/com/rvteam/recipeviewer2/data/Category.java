package com.rvteam.recipeviewer2.data;

public class Category implements IEntity {
    private Integer id;
    private String title;
    public Category(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public Integer getID() {
        return id;
    }
    @Override
    public void setID(int n) {
        this.id = n;
    }
    public String getTitle() { return title; }
}
