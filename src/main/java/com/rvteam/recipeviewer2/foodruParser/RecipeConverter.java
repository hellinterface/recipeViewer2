package com.rvteam.recipeviewer2.foodruParser;

import com.rvteam.recipeviewer2.data.*;
import com.rvteam.recipeviewer2.foodruParser.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeConverter {
    static public Recipe convertFromParserResult(ParserResult result) {
        CategoryRepository categoryRepository = CategoryRepository.getInstance();
        IngredientRepository ingredientRepository = IngredientRepository.getInstance();
        // Категория --------------------------------------------------------------------------------- F
        Category category = new Category(-1, result.getCategory());
        Category trySelect_category = categoryRepository.selectByTitle(result.getCategory());
        if (trySelect_category != null) category = trySelect_category;
        // Фотография -------------------------------------------------------------------------------- 0
        Photo mainPhoto = new Photo(-1, result.getPhoto());
        // Ингредиенты ------------------------------------------------------------------------------- F
        List<IngredientUse> ingredientUses = new ArrayList<IngredientUse>();
        for (var i: result.getIngredients()) {
            Ingredient ingredient = new Ingredient(-1, i.getName(), i.getCalories(), i.getBzuB(), i.getBzuZ(), i.getBzuU());
            Ingredient trySelect_ingredient = ingredientRepository.selectByName(i.getName());
            if (trySelect_ingredient != null) ingredient = trySelect_ingredient;
            IngredientUse ingredientUse = new IngredientUse(-1, ingredient, i.getWeight());
            ingredientUses.add(ingredientUse);
        }
        // Шаги -------------------------------------------------------------------------------------- 0
        List<Step> steps = new ArrayList<Step>();
        for (var i: result.getSteps()) {
            List<Photo> stepPhotos = new ArrayList<Photo>();
            Photo stepPhoto = new Photo(-1, i.getImage());
            stepPhotos.add(stepPhoto);
            Step step = new Step(-1, -1, i.getText(), stepPhotos);
            steps.add(step);
        }

        Recipe recipe = new Recipe(
                -1,
                result.getTitle(),
                result.getDescription(),
                category,
                mainPhoto,
                result.getTime(),
                result.getDifficulty(),
                ingredientUses,
                steps
        );
        return recipe;
    }
}
