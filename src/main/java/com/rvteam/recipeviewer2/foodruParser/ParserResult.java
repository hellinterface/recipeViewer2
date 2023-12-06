package com.rvteam.recipeviewer2.foodruParser;

import java.util.List;

public class ParserResult {
    private String title;
    private String description;
    private List<ParserResultStep> steps;
    private List<ParserResultIngredient> ingredients;
    private Integer time;
    private Integer difficulty;
    private byte[] photo;
    private String category;

    public ParserResult(
            String _title,
            String _description,
            Integer _time,
            Integer _difficulty,
            String _category,
            byte[] _photo,
            List<ParserResultStep> _steps,
            List<ParserResultIngredient> _ingredients) {
        title = _title;
        description = _description;
        time = _time;
        difficulty = _difficulty;
        category = _category;
        photo = _photo;
        steps = _steps;
        ingredients = _ingredients;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Integer getTime() {
        return time;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public List<ParserResultIngredient> getIngredients() {
        return ingredients;
    }

    public List<ParserResultStep> getSteps() {
        return steps;
    }

    public void DEBUG_print() {
        System.out.println("Title: " + getTitle());
        System.out.println("Description: " + getDescription());
        System.out.println("Difficulty: " + getDifficulty());
        System.out.println("Time: " + getTime());
        System.out.println("Category: " + getCategory());
        System.out.println("Ingredients: ");
        for (var i : getIngredients()) {
            System.out.println("    > " + i.getName() + " @ " + i.getWeight());
        }
        System.out.println("Steps: ");
        for (var i : getSteps()) {
            System.out.println("    > Text: " + i.getText());
            System.out.println("      Photo byte length: " + i.getImage().length);
        }
    }
}
