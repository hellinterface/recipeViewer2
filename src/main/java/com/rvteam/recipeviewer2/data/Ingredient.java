package com.rvteam.recipeviewer2.data;

public class Ingredient implements IEntity {
    public String getSqlTableString() {
        return "SQL";
    }
    private Integer id;
    private String name;
    private Float calories;
    private Float bzu_b;
    private Float bzu_z;
    private Float bzu_u;

    public Ingredient(Integer id, String name, Float calories, Float bzu_b, Float bzu_z, Float bzu_u) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.bzu_b = bzu_b;
        this.bzu_z = bzu_z;
        this.bzu_u = bzu_u;
    }
    public Integer getID() { return this.id; }
    public void setID(int n) {
        this.id = n;
    }
    public String getName() { return this.name; }
    public Float getCalories() { return this.calories; }
    public Float getBzuB() { return this.bzu_b; }
    public Float getBzuZ() { return this.bzu_z; }
    public Float getBzuU() { return this.bzu_u; }
}
