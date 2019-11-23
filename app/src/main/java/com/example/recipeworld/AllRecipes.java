package com.example.recipeworld;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllRecipes {
    private static AllRecipes sRecipeLab;
    private List<Recipe> mRecipes;

    public static AllRecipes get(Context context) {
        if(sRecipeLab == null) {
            sRecipeLab = new AllRecipes(context);
        }
        return sRecipeLab;
    }
    private AllRecipes(Context context) {
        /*
        mRecipes = new ArrayList<>();
// initialisation avec des Recipes bidons.
        for(int i = 0; i < 100; i++) {
            Recipe Recipe = new Recipe();
            Recipe.setTitle("Recipe #" + i);
            Recipe.setSolved(i%2==0); // solved every other one
            mRecipes.add(Recipe);
        }
        */

    }
    public Recipe getRecipe(UUID id) {
        for (Recipe Recipe : mRecipes) {
            if(Recipe.getmId().equals(id))
                return Recipe;
        }
        return null;
    }
    public List<Recipe> getRecipes() {
        return mRecipes;
    }
}

