package com.rvteam.recipeviewer2.foodruParser;

public class ParserResultIngredient {
    private String name;
    private Float weight;
    private Float calories;
    private Float bzu_b;
    private Float bzu_z;
    private Float bzu_u;
    public ParserResultIngredient(String _name, Float _weight, Float _calories, Float _b, Float _z, Float _u) {
        this.name = _name;
        this.weight = _weight;
        this.calories = _calories;
        this.bzu_b = _b;
        this.bzu_z = _z;
        this.bzu_u = _u;
    }
    public String getName() {
        return this.name;
    }
    public Float getWeight() {
        return this.weight;
    }
    public Float getCalories() {
        return this.calories;
    }
    public Float getBzuB() {
        return bzu_b;
    }
    public Float getBzuZ() {
        return bzu_z;
    }
    public Float getBzuU() {
        return bzu_u;
    }
}
